/**
 * AttendanceReviewScreen — 근태 조회/결재 화면 (테블릿 터치 최적화)
 *
 * [조회 조건] 설비(생산라인), 일자, 결제구분, 상태
 * [항목]      사번, 성명, 설비, 출근시간, 퇴근시간,
 *             상태(정상/년차/반차/조퇴/지각/이탈),
 *             결제(확인→반장→과장→부장 순차 결재),
 *             전송
 *
 * [사용법] 화면 JSP 안에:
 *   <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/attendance_review.css">
 *   <div id="attendance-root"></div>
 *   <script type="text/babel" src="${pageContext.request.contextPath}/resources/js/widgets/attendance_review.jsx"></script>
 *   <script type="text/babel">
 *     ReactDOM.createRoot(document.getElementById('attendance-root')).render(
 *       <AttendanceReviewScreen
 *         listApi="${pageContext.request.contextPath}/api/attendance"
 *         statusApi="${pageContext.request.contextPath}/api/attendance/status-codes"
 *         equipApi="${pageContext.request.contextPath}/api/attendance/equipments"
 *       />
 *     );
 *   </script>
 *
 * React 18 UMD + Babel standalone가 이미 CDN으로 로드되어 있다는 전제입니다.
 * 기대하는 API 응답/요청 형식은 파일 맨 아래 주석 참고.
 */

const DEFAULT_STATUS_LIST = ['정상', '년차', '반차', '조퇴', '지각', '이탈']; // statusApi 조회 실패 시 사용할 기본값
const DEFAULT_EQUIP_LIST = ['A라인', 'B라인', 'C라인']; // equipApi 조회 실패 시 사용할 기본값 (생산라인만)
const STAGES = ['확인', '반장', '과장', '부장']; // 결제 순서 (index+1 = stage 값)
const TIME_RE = /^([01]\d|2[0-3]):([0-5]\d)$/; // "HH:mm" (00:00~23:59)

function AttendanceReviewScreen({
  listApi = '/api/attendance',
  statusApi = '/api/attendance/status-codes',
  equipApi = '/api/attendance/equipments',
}) {
  const [statusList, setStatusList] = React.useState(DEFAULT_STATUS_LIST); // DB에서 읽어오면 이 값으로 교체됨
  const [equipList, setEquipList] = React.useState(DEFAULT_EQUIP_LIST); // DB에서 읽어오면 이 값으로 교체됨 (생산라인만)
  const [equip, setEquip] = React.useState('전체');
  const [date, setDate] = React.useState(() => new Date().toISOString().slice(0, 10));
  const [payFilter, setPayFilter] = React.useState('전체'); // 전체 | 미결재 | 결재중 | 완료
  const [statusFilter, setStatusFilter] = React.useState('전체'); // 전체 또는 statusList 중 하나(다중선택 원하면 배열로 확장)
  const [rows, setRows] = React.useState([]);
  const [loading, setLoading] = React.useState(false);
  const [openStatusMenuId, setOpenStatusMenuId] = React.useState(null);
  const [selected, setSelected] = React.useState(() => new Set()); // 다중 선택된 row id
  const [original, setOriginal] = React.useState({}); // id -> 조회 시점의 원본값 (변경 여부 판단용)
  const [errorMsg, setErrorMsg] = React.useState(''); // 서버가 거부(400 등)했을 때 보여줄 메시지

  React.useEffect(() => {
    if (!errorMsg) return;
    const t = setTimeout(() => setErrorMsg(''), 4000); // 4초 후 자동으로 사라짐
    return () => clearTimeout(t);
  }, [errorMsg]);

  // 서버에서 IllegalArgumentException/IllegalStateException이 발생하면 보통 400(Bad Request)으로
  // 응답하면서 그 예외 메시지를 body(텍스트)에 그대로 담아 보냅니다. fetch()는 400/500 같은
  // HTTP 에러 상태에서는 reject하지 않고 그냥 resolve하므로, res.ok를 직접 확인해야 합니다.
  const handleFail = res => {
    if (res.ok) return false;
    res.text().then(msg => setErrorMsg(msg || `요청이 거부됐습니다 (${res.status})`));
    load(); // 화면에 미리 반영해둔 값(낙관적 업데이트)을 서버 기준으로 되돌림
    return true;
  };

  const load = React.useCallback(() => {
    setLoading(true);
    const params = new URLSearchParams({ equip, date, pay: payFilter, status: statusFilter });
    fetch(`${listApi}?${params}`, { credentials: 'same-origin' })
      .then(r => (r.ok ? r.json() : Promise.reject(r.status)))
      .then(data => {
        const list = Array.isArray(data) ? data : data.items || [];
        setRows(list);
        const snapshot = {};
        list.forEach(r => {
          snapshot[r.id] = { checkIn: r.checkIn, checkOut: r.checkOut, status: r.status, equip: r.equip };
        });
        setOriginal(snapshot); // 조회할 때마다 "변경됨" 표시를 초기화
      })
      .catch(() => setRows([]))
      .finally(() => setLoading(false));
  }, [listApi, equip, date, payFilter, statusFilter]);

  // 출근시간/퇴근시간/상태/설비가 조회 시점 값과 달라졌으면 true (해당 칸을 색으로 강조)
  const isChanged = (row, field) => {
    const base = original[row.id];
    return !!base && base[field] !== row[field];
  };

  // 네 항목 중 하나라도 바뀌었으면 true → 그 행(라인) 전체를 색으로 강조
  const isRowChanged = row =>
    isChanged(row, 'checkIn') || isChanged(row, 'checkOut') || isChanged(row, 'equip') || isChanged(row, 'status');

  React.useEffect(() => { load(); }, []); // 최초 1회 조회

  // 상태 목록(정상/년차/반차/...)을 DB에서 읽어와 교체 — 실패하면 DEFAULT_STATUS_LIST를 그대로 씀
  React.useEffect(() => {
    fetch(statusApi, { credentials: 'same-origin' })
      .then(r => (r.ok ? r.json() : Promise.reject(r.status)))
      .then(data => {
        const list = Array.isArray(data) ? data : data.items || [];
        if (list.length > 0) setStatusList(list);
      })
      .catch(() => {}); // 실패해도 화면은 DEFAULT_STATUS_LIST로 계속 동작
  }, [statusApi]);

  // 설비(생산라인) 목록을 DB에서 읽어와 교체 — 실패하면 DEFAULT_EQUIP_LIST를 그대로 씀
  React.useEffect(() => {
    fetch(equipApi, { credentials: 'same-origin' })
      .then(r => (r.ok ? r.json() : Promise.reject(r.status)))
      .then(data => {
        const list = Array.isArray(data) ? data : data.items || [];
        if (list.length > 0) setEquipList(list);
      })
      .catch(() => {});
  }, [equipApi]);

  const changeStatus = (row, status) => {
    setOpenStatusMenuId(null);
    setRows(prev => prev.map(r => (r.id === row.id ? { ...r, status } : r)));
    fetch(`${listApi}/${row.id}/status`, {
      method: 'PATCH',
      credentials: 'same-origin',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status }),
    }).then(handleFail).catch(() => load()); // 실패하면 서버 기준으로 재조회
  };

  // 설비 수정 (select라 값이 항상 유효하므로 바로 반영)
  const changeField = (row, field, value, endpoint) => {
    setRows(prev => prev.map(r => (r.id === row.id ? { ...r, [field]: value } : r)));
    fetch(`${listApi}/${row.id}/${endpoint}`, {
      method: 'PATCH',
      credentials: 'same-origin',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ [field]: value }),
    }).then(handleFail).catch(() => load());
  };

  // 출근/퇴근시간은 입력 중(타이핑)에는 화면 값만 바꾸고, 입력을 끝냈을 때(blur)만 서버로 반영합니다.
  // (일부 테블릿 브라우저에서 <input type="time">의 네이티브 시간선택기가 탭에 반응하지 않는
  //  경우가 있어, 숫자 키패드로 직접 입력하는 일반 텍스트 입력으로 대체했습니다.)
  const editTimeLocal = (rowId, field, rawValue) => {
    // 숫자와 ':'만 남기고, 최대 5자("HH:mm")로 제한
    let v = rawValue.replace(/[^0-9:]/g, '').slice(0, 5);
    setRows(prev => prev.map(r => (r.id === rowId ? { ...r, [field]: v } : r)));
  };

  const commitTime = (row, field, endpoint) => {
    const value = row[field] || '';
    if (value !== '' && !TIME_RE.test(value)) {
      // 형식이 안 맞으면 저장하지 않고 원래 값(조회 시점 값)으로 되돌립니다.
      const fallback = (original[row.id] && original[row.id][field]) || '';
      setRows(prev => prev.map(r => (r.id === row.id ? { ...r, [field]: fallback } : r)));
      return;
    }
    fetch(`${listApi}/${row.id}/${endpoint}`, {
      method: 'PATCH',
      credentials: 'same-origin',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ [field]: value }),
    }).then(handleFail).catch(() => load());
  };

  const approveNext = row => {
    if (row.stage >= STAGES.length) return;
    const nextStage = row.stage + 1;
    setRows(prev => prev.map(r => (r.id === row.id ? { ...r, stage: nextStage } : r)));
    fetch(`${listApi}/${row.id}/approve`, {
      method: 'PATCH',
      credentials: 'same-origin',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ stage: nextStage }),
    }).catch(() => load());
  };

  const send = row => {
    if (row.stage < STAGES.length || row.sent) return;
    setRows(prev => prev.map(r => (r.id === row.id ? { ...r, sent: true } : r)));
    fetch(`${listApi}/${row.id}/send`, { 
	  	method: 'POST', 
	  	credentials: 'same-origin' ,
	  	headers: { 'Content-Type': 'application/json' },
	  	body: JSON.stringify({}),
	  }).catch(() => load());
  };

  // ── 다중 선택 ──────────────────────────────────────────────
  const toggleSelect = id => {
    setSelected(prev => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };
  const toggleSelectAll = () => {
    setSelected(prev => (prev.size === rows.length ? new Set() : new Set(rows.map(r => r.id))));
  };
  const clearSelection = () => setSelected(new Set());

  // 선택된 건 중 아직 결재 남은 것들을 한 번에 다음 단계로 승인
  const bulkApprove = () => {
    const targets = rows
      .filter(r => selected.has(r.id) && r.stage < STAGES.length)
      .map(r => ({ id: r.id, nextStage: r.stage + 1 }));
    if (targets.length === 0) return;
    setRows(prev => prev.map(r => {
      const t = targets.find(t => t.id === r.id);
      return t ? { ...r, stage: t.nextStage } : r;
    }));
    targets.forEach(t => {
      fetch(`${listApi}/${t.id}/approve`, {
        method: 'PATCH',
        credentials: 'same-origin',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ stage: t.nextStage }),
      }).then(handleFail).catch(() => load());
    });
  };

  // 선택된 건 중 결재가 끝나 전송 가능한 것들을 한 번에 전송
  const bulkSend = () => {
    const targets = rows
      .filter(r => selected.has(r.id) && r.stage >= STAGES.length && !r.sent)
      .map(r => r.id);
    if (targets.length === 0) return;
    setRows(prev => prev.map(r => (targets.includes(r.id) ? { ...r, sent: true } : r)));
    targets.forEach(id => {
      fetch(`${listApi}/${id}/send`, { method: 'POST', credentials: 'same-origin' })
        .then(handleFail).catch(() => load());
    });
  };

  const canBulkApprove = rows.some(r => selected.has(r.id) && r.stage < STAGES.length);
  const canBulkSend = rows.some(r => selected.has(r.id) && r.stage >= STAGES.length && !r.sent);

  return (
    <div className="att-screen">
      {/* 서버가 요청을 거부했을 때(400 등) IllegalArgumentException 메시지를 보여주는 배너 */}
      {errorMsg && (
        <div className="att-error-banner" onClick={() => setErrorMsg('')}>
          ⚠ {errorMsg}
        </div>
      )}

      {/* 조회 조건 */}
      <div className="att-filter-card">
        <div className="att-filter-row">
          <div className="att-filter-group">
            <span className="att-filter-label">설비</span>
            <select className="att-select" value={equip} onChange={e => setEquip(e.target.value)}>
              {['전체', ...equipList].map(opt => (
                <option key={opt} value={opt}>{opt}</option>
              ))}
            </select>
          </div>
          <div className="att-filter-group">
            <span className="att-filter-label">일자</span>
            <input type="date" className="att-date" value={date} onChange={e => setDate(e.target.value)} />
          </div>
          <div className="att-filter-group">
            <span className="att-filter-label">결제구분</span>
            <div className="att-chip-group">
              {['전체', '미결재', '결재중', '완료'].map(opt => (
                <button
                  key={opt}
                  className={`att-chip ${payFilter === opt ? 'active' : ''}`}
                  onClick={() => setPayFilter(opt)}
                >
                  {opt}
                </button>
              ))}
            </div>
          </div>
          <div className="att-spacer" />
          <button className="att-btn-search" onClick={load} disabled={loading}>
            {loading ? '조회 중...' : '🔍 조회'}
          </button>
        </div>

        <div className="att-filter-row att-status-filter-row">
          <span className="att-filter-label">상태</span>
          <div className="att-chip-group">
            {['전체', ...statusList].map(opt => (
              <button
                key={opt}
                className={`att-chip ${statusFilter === opt ? 'active' : ''}`}
                onClick={() => setStatusFilter(opt)}
              >
                {opt}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* 목록 */}
      <div className="att-table-card">
        <table className="att-table">
          <thead>
            <tr>
              <th className="att-checkbox-col">
                <input
                  type="checkbox"
                  className="att-row-checkbox"
                  checked={rows.length > 0 && selected.size === rows.length}
                  onChange={toggleSelectAll}
                />
              </th>
              <th style={{ width: 80 }}>사번</th>
              <th style={{ width: 100 }}>성명</th>
              <th style={{ width: 90 }}>설비</th>
              <th style={{ width: 90 }}>출근시간</th>
              <th style={{ width: 90 }}>퇴근시간</th>
              <th style={{ width: 120 }}>상태</th>
              <th style={{ width: 260 }}>결제</th>
              <th style={{ width: 100 }}>전송</th>
            </tr>
          </thead>
          <tbody>
            {!loading && rows.length === 0 && (
              <tr>
                <td colSpan={9} style={{ padding: '32px 0', color: '#adb5bd' }}>조회된 내역이 없습니다.</td>
              </tr>
            )}
            {rows.map(row => (
              <tr
                key={row.id}
                className={`${selected.has(row.id) ? 'att-selected' : ''} ${isRowChanged(row) ? 'att-row-changed' : ''}`}
              >
                <td>
                  <input
                    type="checkbox"
                    className="att-row-checkbox"
                    checked={selected.has(row.id)}
                    onChange={() => toggleSelect(row.id)}
                  />
                </td>
                <td className="att-empno">{row.empNo}</td>
                <td className="att-name">{row.name}</td>
                <td>
                  <select
                    className={`att-equip-select ${isChanged(row, 'equip') ? 'att-field-changed' : ''}`}
                    value={row.equip}
                    onChange={e => changeField(row, 'equip', e.target.value, 'equip')}
                  >
                    {equipList.map(opt => (
                      <option key={opt} value={opt}>{opt}</option>
                    ))}
                  </select>
                </td>
                <td>
                  <input
                    type="text"
                    inputMode="numeric"
                    placeholder="--:--"
                    maxLength={5}
                    className={`att-time-input ${isChanged(row, 'checkIn') ? 'att-field-changed' : ''}`}
                    value={row.checkIn || ''}
                    onChange={e => editTimeLocal(row.id, 'checkIn', e.target.value)}
                    onBlur={() => commitTime(row, 'checkIn', 'checkin')}
                  />
                </td>
                <td>
                  <input
                    type="text"
                    inputMode="numeric"
                    placeholder="--:--"
                    maxLength={5}
                    className={`att-time-input ${isChanged(row, 'checkOut') ? 'att-field-changed' : ''}`}
                    value={row.checkOut || ''}
                    onChange={e => editTimeLocal(row.id, 'checkOut', e.target.value)}
                    onBlur={() => commitTime(row, 'checkOut', 'checkout')}
                  />
                </td>

                <td>
                  <button
                    className={`att-status-pill att-status-${row.status} ${isChanged(row, 'status') ? 'att-field-changed' : ''}`}
                    onClick={() => setOpenStatusMenuId(openStatusMenuId === row.id ? null : row.id)}
                  >
                    {row.status}
                  </button>
                  {openStatusMenuId === row.id && (
                    <div className="att-status-menu">
                      {statusList.map(s => (
                        <button
                          key={s}
                          className={`att-status-${s}`}
                          onClick={() => changeStatus(row, s)}
                        >
                          {s}
                        </button>
                      ))}
                    </div>
                  )}
                </td>

                <td>
                  <div className="att-stepper">
                    {STAGES.map((label, i) => {
                      const stageNum = i + 1;
                      const done = stageNum <= row.stage;
                      const isNext = stageNum === row.stage + 1;
                      return (
                        <React.Fragment key={label}>
                          <div className="att-step">
                            <div
                              className={`att-step-dot ${done ? 'done' : ''} ${isNext ? 'next' : ''}`}
                              onClick={() => isNext && approveNext(row)}
                              title={isNext ? `${label} 결재` : undefined}
                            >
                              {done ? '✓' : stageNum}
                            </div>
                            <div className={`att-step-label ${done ? 'done' : ''}`}>{label}</div>
                          </div>
                          {i < STAGES.length - 1 && <div className={`att-step-line ${stageNum < row.stage ? 'done' : ''}`} />}
                        </React.Fragment>
                      );
                    })}
                  </div>
                </td>

                <td>
                  {row.sent ? (
                    <button className="att-btn-send done" disabled>완료</button>
                  ) : row.stage >= STAGES.length ? (
                    <button className="att-btn-send ready" onClick={() => send(row)}>전송</button>
                  ) : (
                    <button className="att-btn-send disabled" disabled>전송</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 다중 선택 시에만 나타나는 일괄 처리 바 */}
      {selected.size > 0 && (
        <div className="att-bulk-bar">
          <span className="att-bulk-count">{selected.size}건 선택됨</span>
          <div className="att-bulk-spacer" />
          <button className="att-bulk-btn ghost" onClick={clearSelection}>선택 해제</button>
          <button className="att-bulk-btn primary" onClick={bulkApprove} disabled={!canBulkApprove}>일괄 결제</button>
          <button className="att-bulk-btn primary" onClick={bulkSend} disabled={!canBulkSend}>일괄 전송</button>
        </div>
      )}
    </div>
  );
}

/*
 * ── 기대 API 형식 ─────────────────────────────────────────────────
 * GET {statusApi}
 *   → ["정상", "년차", "반차", "조퇴", "지각", "이탈"]  (DB의 상태 코드 목록, 순서대로)
 *
 * GET {equipApi}
 *   → ["A라인", "B라인", "C라인"]  (DB의 설비=생산라인 목록, "전체"는 화면에서 자동으로 맨 앞에 붙임)
 *
 * GET {listApi}?equip=&date=&pay=&status=
 *   → [{ id, empNo, name, equip, checkIn, checkOut, status, stage, sent }, ...]
 *     - status ∈ '정상'|'년차'|'반차'|'조퇴'|'지각'|'이탈'
 *     - stage  ∈ 0~4 (0=미결재, 1=확인완료, 2=반장까지, 3=과장까지, 4=부장까지=결재완료)
 *     - sent   ∈ boolean (전송 완료 여부)
 *
 * PATCH {listApi}/{id}/status    body: { status }         → 200 OK
 * PATCH {listApi}/{id}/checkin   body: { checkIn }        → 200 OK  (출근시간 수정, "HH:mm")
 * PATCH {listApi}/{id}/checkout  body: { checkOut }       → 200 OK  (퇴근시간 수정, "HH:mm")
 * PATCH {listApi}/{id}/equip     body: { equip }          → 200 OK  (설비 변경)
 * PATCH {listApi}/{id}/approve   body: { stage }          → 200 OK  (순차 결재, stage=현재 결재자 단계)
 * POST  {listApi}/{id}/send                               → 200 OK  (stage=4 완료 건만 허용)
 *
 * [일괄 결제/전송] 현재는 선택된 건마다 위 approve/send를 각각 병렬 호출합니다.
 * 건수가 많아지면 서버에 배치 엔드포인트를 추가하는 게 좋습니다.
 *   PATCH {listApi}/bulk-approve  body: { ids: number[] }  → 각 id를 다음 결재 단계로 일괄 승인
 *   POST  {listApi}/bulk-send     body: { ids: number[] }  → stage=4인 id들만 일괄 전송
 * (배치 엔드포인트를 만들면 위 bulkApprove/bulkSend 함수의 forEach 호출부만 이걸로 교체하면 됩니다.)
 *
 * Spring MVC 컨트롤러 매핑 예시 (com.sens.lush):
 *   @GetMapping("/api/attendance")
 *   @ResponseBody
 *   List<AttendanceDto> list(String equip, String date, String pay, String status) { ... }
 *
 *   @PatchMapping("/api/attendance/{id}/status")
 *   @PatchMapping("/api/attendance/{id}/approve")
 *   @PostMapping("/api/attendance/{id}/send")
 * ─────────────────────────────────────────────────────────────────
 */
