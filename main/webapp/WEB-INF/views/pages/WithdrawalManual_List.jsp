<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*"
    %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="path" value="${pageContext.request.contextPath }"/>

<div style="padding: 10px; background: white; border-radius: 5px;">
<div style="background: lightgray; border-radius: 5px; text-align: center; height: 80px; align-content: center; color:black;">
  <h2>탈퇴회원 대응 방안 리스트</h2>

</div>
<div style="background: white;">
	
   <form id="dataFrm" style="width:100%; overflow: hidden;">
   <table class="table table-notice-hover" style="width:100%;">
   	<col width="5%">
   	<col width="10%">
   	<col width="25%">
   	<col width="20%">
   	<col width="20%">
   	<col width="10%">   	
   	<col width="10%">
   	
    <thead >
    <%-- id; reason;  situation; checkAction;  guidance; followUp; --%>
      <tr class="text-center" style="background: black; color:white;">
        <th style="color:white;">Id</th>
        <th style="color:white;">탈퇴 원인</th>
        <th style="color:white;">상황</th>
        <th style="color:white;">확인</th>
        <th style="color:white;">안내</th>
        <th style="color:white;">후속 조치</th>
        <th style="color:white;">기능</th>
      </tr>
    </thead>	
    <tbody>
    
    	<c:forEach var="manual" items="${list}" varStatus="sts">
    
    		<tr ondblclick="goDetail(${manual.id})"><td>${manual.id}</td><td>${manual.reason}</td>
    			<td>${manual.situation}</td><td>${manual.checkAction}</td>
    			<td>
    				<textarea name="guidance" style="width: 150px; height: 50px;">${manual.guidance}</textarea>
    			</td>
    			<td>
    			<textarea name="followUp" style="width: 150px; height: 50px;">${manual.followUp}</textarea>
    			</td>
    			<td>
    				<button onclick="uptData(${manual.id},${sts.index})" id="uptBtn" type="button" class="btn btn-warning">수정</button>
            		<button onclick="delData(${manual.id})"  id="delBtn"  type="button" class="btn btn-danger">삭제</button>
    			
    			</td>
    			</tr>
    	</c:forEach>
    
    </tbody>
    
	</table>   
	</form> 
	<form id="chData" method="post">
		<input type="hidden" name="id"/>
		<input type="hidden" name="guidance"/>
		<input type="hidden" name="followUp"/>
	</form>
	<script type="text/javascript">
		function uptData(id, idx){
			//alert(id+":"+idx)
			//alert($("#dataFrm [name=guidance]").eq(idx).val()+":"+ $("#dataFrm [name=followUp]").eq(idx).val())
			
			if(confirm("수정하시겠습니까?")){
				$("#chData [name=guidance]").val( $("#dataFrm [name=guidance]").eq(idx).val() );
				$("#chData [name=followUp]").val( $("#dataFrm [name=followUp]").eq(idx).val() );
				$("#chData [name=id]").val( id );
				$("#chData").attr("action", "withdrawalManualList_Update") // controller  updateData 가 필요
				$("#chData").submit()
			}
			
		}
		function delData(id){
			if(confirm("삭제하시겠습니까?")){
				$("#chData [name=id]").val( id );
				$("#chData").attr("action", "withdrawalManualList_Delete") // controller  updateData 가 필요
				$("#chData").submit()
			}
		}		
		
		function goDetail(id){
			location.href="withdrawalManual_Detail?id="+id
		}
	</script>
    
</div>
</div>