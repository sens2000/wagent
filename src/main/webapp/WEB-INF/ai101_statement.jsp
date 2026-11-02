<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>거래명세서</title>
<style>
  * { box-sizing: border-box; }
  body {
    font-family: "맑은 고딕", "Malgun Gothic", sans-serif;
    color: #222;
    background: #e9e9e9;
    margin: 0;
    padding: 20px;
  }

  /* 화면에서만 보이는 툴바 (인쇄 시 숨김) */
  .toolbar {
    max-width: 900px;
    margin: 0 auto 12px;
    text-align: right;
  }
  .toolbar button {
    padding: 6px 16px;
    font-size: 0.9rem;
    border: 1px solid #888;
    background: #fff;
    border-radius: 4px;
    cursor: pointer;
    margin-left: 6px;
  }
  .toolbar button.primary {
    background: #0d6efd;
    color: #fff;
    border-color: #0d6efd;
  }

  .sheet {
    max-width: 900px;
    margin: 0 auto;
    background: #fff;
    padding: 30px 36px;
    box-shadow: 0 0 6px rgba(0,0,0,0.15);
  }

  .title {
    text-align: center;
    font-size: 1.6rem;
    font-weight: 700;
    letter-spacing: 12px;
    margin: 0 0 20px;
  }

  .meta-row {
    display: flex;
    justify-content: space-between;
    align-items: flex-end;
    margin-bottom: 10px;
    font-size: 0.9rem;
  }
  .meta-row .doc-no { color: #555; }

  /* 공급받는자 / 공급자 정보 - 좌우 2단 배치 */
  .party-grid {
    display: flex;
    gap: 12px;
    margin-bottom: 16px;
  }
  .party-box {
    flex: 1;
    min-width: 0;
  }
  .party-box-title {
    border: 1px solid #333;
    border-bottom: none;
    background: #f2f2f2;
    text-align: center;
    font-weight: 700;
    padding: 5px;
    font-size: 0.92rem;
  }
  .party-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.86rem;
    table-layout: fixed;
  }
  .party-table th, .party-table td {
    border: 1px solid #333;
    padding: 6px 8px;
    text-align: left;
    vertical-align: middle;
  }
  .party-table th {
    background: #f2f2f2;
    font-weight: 600;
    text-align: center;
    white-space: nowrap;
  }
  .party-table .seal-cell {
    color: #999;
  }
  .party-table td.rep-cell {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .section-title {
    font-weight: 700;
    font-size: 0.95rem;
    margin: 4px 0 6px;
    padding-bottom: 3px;
    border-bottom: 2px solid #333;
  }

  /* 품목 테이블 */
  .item-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 0.86rem;
    margin-bottom: 4px;
  }
  .item-table th, .item-table td {
    border: 1px solid #333;
    padding: 6px 6px;
  }
  /* 품목이 많아 2페이지 이상으로 인쇄될 때: 헤더(거래처/공급자 정보 포함)는 매 페이지 위에,
     당월현황은 매 페이지 아래에 반복해서 찍히고, 한 행이 페이지 경계에서 잘리지 않도록 한다. */
  .item-table thead {
    display: table-header-group;
  }
  .item-table tfoot {
    display: table-footer-group;
  }
  .item-table tbody tr {
    page-break-inside: avoid;
  }
  .item-table tr.repeat-row > td {
    border: none;
    padding: 0;
  }
  .item-table tr.repeat-row .party-grid {
    margin-top: 10px;
  }
  /* thead/tfoot 안에 넣은 party-table, summary-table은 item-table의 셀 스타일을
     물려받지 않고 자기 고유 스타일(border, padding)을 그대로 쓰도록 한다. */
  .item-table tr.repeat-row .party-table th,
  .item-table tr.repeat-row .party-table td,
  .item-table tr.repeat-row .summary-table th,
  .item-table tr.repeat-row .summary-table td {
    padding: 6px 8px;
  }
  .item-table thead th {
    background: #f2f2f2;
    text-align: center;
    font-weight: 600;
  }
  .item-table tbody td {
    text-align: center;
  }
  .item-table tbody td.name-cell {
    text-align: left;
  }
  .item-table tbody td.num-cell {
    text-align: right;
    white-space: nowrap;
  }
  .item-table tbody tr.empty-row td {
    height: 24px;
  }
  .item-table tfoot td {
    font-weight: 700;
    background: #fafafa;
  }
  .item-table tfoot tr {
    page-break-inside: avoid;
  }
  .summary-table {
    page-break-inside: avoid;
  }

  /* 하단 미수현황 요약 */
  .summary-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 14px;
    font-size: 0.86rem;
    table-layout: fixed;
  }
  .summary-table th, .summary-table td {
    border: 1px solid #333;
    padding: 6px 8px;
    vertical-align: middle;
  }
  .summary-table th {
    background: #f2f2f2;
    font-weight: 600;
    text-align: center;
    white-space: nowrap;
    width: 20%;
  }
  .summary-table td.num-cell {
    text-align: right;
  }
  .summary-table td.confirm-cell {
    text-align: center;
  }

  .footer-note {
    margin-top: 16px;
    font-size: 0.82rem;
    color: #555;
    line-height: 1.6;
  }

  @media print {
    body { background: #fff; padding: 0; }
    .toolbar { display: none; }
    .sheet { box-shadow: none; padding: 0; max-width: none; }
    @page { size: A4; margin: 15mm; }
  }
</style>
</head>
<body>

  <div class="toolbar no-print">
    <button type="button" onclick="window.print();">인쇄</button>
    <button type="button" class="primary" onclick="window.close();">닫기</button>
  </div>

  <div class="sheet">

    <div class="title">거 래 명 세 서</div>

    <div class="meta-row">
      <div>
        거래일자 :
        <c:choose>
          <c:when test="${not empty ipdate}">
            <c:out value="${ipdate}"/>
          </c:when>
          <c:otherwise>-</c:otherwise>
        </c:choose>
      </div>
    </div>

    <!-- 세부내용 -->
    <table class="item-table">
      <thead>
        <!-- 공급받는자 / 공급자 정보 : 페이지가 나뉠 때마다 매 페이지 위에 반복해서 찍힌다 -->
        <tr class="repeat-row">
          <td colspan="8">
            <div class="party-grid">
              <div class="party-box">
                <div class="party-box-title">공급받는자</div>
                <table class="party-table">
                  <colgroup>
                    <col style="width:18%">
                    <col style="width:32%">
                    <col style="width:18%">
                    <col style="width:32%">
                  </colgroup>
                  <tr>
                    <th>거래처명</th>
                    <td><c:out value="${custHeader.CUSTNAME}" default=""/></td>
                    <th>사업자번호</th>
                    <td><c:out value="${custHeader.SANO}" default=""/></td>
                  </tr>
                  <tr>
                    <th>대표</th>
                    <td><c:out value="${custHeader.OWNER}" default=""/></td>
                    <th>전화번호</th>
                    <td><c:out value="${custHeader.HANDNO}" default=""/></td>
                  </tr>
                  <tr>
                    <th>주소</th>
                    <td colspan="3"><c:out value="${custHeader.ADDR}" default=""/></td>
                  </tr>
                </table>
              </div>

              <div class="party-box">
                <div class="party-box-title">공급자</div>
                <table class="party-table">
                  <colgroup>
                    <col style="width:18%">
                    <col style="width:32%">
                    <col style="width:18%">
                    <col style="width:32%">
                  </colgroup>
                  <tr>
                    <th>회사명</th>
                    <td><c:out value="${company.NAME}" default=""/></td>
                    <th>사업자번호</th>
                    <td><c:out value="${company.SANO}" default=""/></td>
                  </tr>
                  <tr>
                    <th>대표(직인)</th>
                    <td class="rep-cell">
                      <c:out value="${company.OWNER}" default=""/>
                      <span class="seal-cell">(인)</span>
                    </td>
                    <th>전화번호</th>
                    <td><c:out value="${company.TEL}" default=""/></td>
                  </tr>
                  <tr>
                    <th>주소</th>
                    <td colspan="3"><c:out value="${company.ADDR}" default=""/></td>
                  </tr>
                </table>
              </div>
            </div>
            <div class="section-title">세부내용</div>
          </td>
        </tr>
        <tr>
          <th style="width:6%;">순번</th>
          <th style="width:12%;">제품코드</th>
          <th style="width:22%;">품명</th>
          <th style="width:10%;">정가</th>
          <th style="width:8%;">수량</th>
          <th style="width:12%;">단가</th>
          <th style="width:14%;">금액</th>
          <th style="width:16%;">비고</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="p" items="${products}" varStatus="st">
          <tr>
            <td>${st.index + 1}</td>
            <td>${p.JEPMCD}</td>
            <td class="name-cell">${p.JEPMNM}</td>
            <td class="num-cell"><fmt:formatNumber value="${p.PRICE}" pattern="#,##0"/></td>
            <td class="num-cell"><fmt:formatNumber value="${p.QTY}" pattern="#,##0"/></td>
            <td class="num-cell"><fmt:formatNumber value="${p.SALEPRICE}" pattern="#,##0"/></td>
            <td class="num-cell"><fmt:formatNumber value="${p.AMT}" pattern="#,##0"/></td>
            <td>${p.REMK}</td>
          </tr>
        </c:forEach>

        <!-- 인쇄 시 표가 너무 짧아 보이지 않도록 최소 8줄 확보 -->
        <c:if test="${fn:length(products) < 15}">
          <c:forEach begin="${fn:length(products)}" end="14">
            <tr class="empty-row">
              <td colspan="8">&nbsp;</td>
            </tr>
          </c:forEach>
        </c:if>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="6" style="text-align:right;">합&nbsp;&nbsp;계</td>
          <td class="num-cell"><fmt:formatNumber value="${totalAmt}" pattern="#,##0"/></td>
          <td></td>
        </tr>
        <!-- 당월현황 : 페이지가 나뉠 때마다 매 페이지 아래에 반복해서 찍힌다 -->
        <tr class="repeat-row">
          <td colspan="8">
            <div class="section-title">당월현황</div>
            <table class="summary-table">
              <thead>
                <tr>
                  <th>당월미수</th>
                  <th>당월매출</th>
                  <th>당월수금</th>
                  <th>현미수</th>
                  <th>확인</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="num-cell"><fmt:formatNumber value="${misu.EWOL}" pattern="#,##0"/></td>
                  <td class="num-cell"><fmt:formatNumber value="${misu.AMT}" pattern="#,##0"/></td>
                  <td class="num-cell"><fmt:formatNumber value="${misu.SKAMT}" pattern="#,##0"/></td>
                  <td class="num-cell"><fmt:formatNumber value="${misu.JANAMT}" pattern="#,##0"/></td>
                  <td class="confirm-cell">&nbsp;</td>
                </tr>
              </tbody>
            </table>
          </td>
        </tr>
      </tfoot>
    </table>

    <div class="footer-note">
      ※ 본 명세서는 <c:out value="${ipdate}"/> 기준 거래 내역입니다. 문의사항은 공급자에게 연락 주시기 바랍니다.
    </div>

  </div>

</body>
</html>