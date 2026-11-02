/**
 * 대시보드 React 위젯 (통계 카드 + 부서별 사원 수 차트)
 * Chart.js를 사용하여 막대/도넛 차트를 그린다.
 *
 * s_boards.do와 동일한 컨벤션: 서버가 success/data 래퍼 없이
 * 통계 객체를 바로 응답하므로, fetch 결과를 그대로 데이터로 사용한다.
 *
 * 필요 조건: React, ReactDOM, @babel/standalone, Chart.js 가 이 스크립트보다 먼저 로드되어 있어야 함
 *   <script src="https://unpkg.com/react@18/umd/react.production.min.js"></script>
 *   <script src="https://unpkg.com/react-dom@18/umd/react-dom.production.min.js"></script>
 *   <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>
 *   <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.4/dist/chart.umd.min.js"></script>
 *   <script type="text/babel" src="react-widget/dashboard-widget.jsx"></script>
 *
 * 사용법:
 *   <div id="dashboard-root"></div>
 *   <script>
 *     DashboardWidget.mount("dashboard-root", { apiUrl: "s_dashboard.do" });
 *   </script>
 */
(function () {
  const { useState, useEffect, useRef } = React;

  // 통계 카드 하나
  function StatCard(props) {
    return (
      <div className="col-6 col-md-3">
        <div className="card p-3 h-100">
          <div className="text-muted small">{props.label}</div>
          <div className="fs-4 fw-bold">{props.value}</div>
        </div>
      </div>
    );
  }

  // 부서별 막대 차트
  function DepartmentBarChart(props) {
    const canvasRef = useRef(null);
    const chartRef = useRef(null);

    useEffect(function () {
      if (!canvasRef.current || !props.data) return;

      if (chartRef.current) {
        chartRef.current.destroy();
      }

      chartRef.current = new Chart(canvasRef.current, {
        type: "bar",
        data: {
          labels: props.data.map(function (d) { return d.dept; }),
          datasets: [{
            label: "부서별 사원 수",
            data: props.data.map(function (d) { return d.count; }),
            backgroundColor: "#0d6efd"
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false }
          },
          scales: {
            y: { beginAtZero: true, ticks: { precision: 0 } }
          }
        }
      });

      return function () {
        if (chartRef.current) {
          chartRef.current.destroy();
          chartRef.current = null;
        }
      };
    }, [props.data]);

    return (
      <div style={{ position: "relative", height: 280 }}>
        <canvas ref={canvasRef}></canvas>
      </div>
    );
  }

  // 부서별 비중 도넛 차트
  function DepartmentDonutChart(props) {
    const canvasRef = useRef(null);
    const chartRef = useRef(null);

    useEffect(function () {
      if (!canvasRef.current || !props.data) return;

      if (chartRef.current) {
        chartRef.current.destroy();
      }

      const colors = ["#0d6efd", "#20c997", "#ffc107", "#fd7e14", "#6f42c1", "#dc3545"];

      chartRef.current = new Chart(canvasRef.current, {
        type: "doughnut",
        data: {
          labels: props.data.map(function (d) { return d.dept; }),
          datasets: [{
            data: props.data.map(function (d) { return d.count; }),
            backgroundColor: props.data.map(function (_, i) { return colors[i % colors.length]; })
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: "bottom", labels: { boxWidth: 12, font: { size: 11 } } }
          }
        }
      });

      return function () {
        if (chartRef.current) {
          chartRef.current.destroy();
          chartRef.current = null;
        }
      };
    }, [props.data]);

    return (
      <div style={{ position: "relative", height: 280 }}>
        <canvas ref={canvasRef}></canvas>
      </div>
    );
  }

  function Dashboard(props) {
    const apiUrl = props.apiUrl || "s_dashboardStat.do";

    const [data, setData] = useState(null);
    const [errorMsg, setErrorMsg] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(function () {
      // s_boards.do와 동일한 패턴: res.json() 결과를 그대로 데이터로 사용
      fetch(apiUrl)
        .then(function (res) { return res.json(); })
        .then(function (result) {
          setData(result);
        })
        .catch(function () {
          setErrorMsg("대시보드 데이터를 불러오지 못했습니다.");
        })
        .finally(function () {
          setLoading(false);
        });
    }, [apiUrl]);

    if (loading) {
      return <div className="text-muted small">불러오는 중...</div>;
    }

    if (errorMsg) {
      return <div className="alert alert-danger py-2 small">{errorMsg}</div>;
    }

    if (!data) {
      return null;
    }

    return (
      <div>
        <div className="row g-3 mb-3">
          <StatCard label="전체 사원" value={data.totalEmployees + "명"} />
          <StatCard label="금월 등록" value={data.thisMonthCount + "명"} />
          <StatCard label="공지사항" value={data.noticeCount + "건"} />
          <StatCard label="미처리 요청" value={data.pendingCount + "건"} />
        </div>

        <div className="row g-3">
          <div className="col-md-7">
            <div className="card p-3 h-100">
              <div className="fw-bold small mb-2">부서별 사원 수</div>
              <DepartmentBarChart data={data.byDepartment} />
            </div>
          </div>
          <div className="col-md-5">
            <div className="card p-3 h-100">
              <div className="fw-bold small mb-2">부서별 비중</div>
              <DepartmentDonutChart data={data.byDepartment} />
            </div>
          </div>
        </div>
      </div>
    );
  }

  window.DashboardWidget = {
    mount: function (elementId, props) {
      const root = ReactDOM.createRoot(document.getElementById(elementId));
      root.render(<Dashboard {...(props || {})} />);
    }
  };
})();