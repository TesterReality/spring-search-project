window.onload=function(){
$(document).ready(function(e) {   
  // your code here

let paginationLeftPos = "20px";
let paginationOpacity = 0;
let checkPaginationClick = 0;
if (checkPaginationClick) {
  paginationOpacity = 1;
} else {
  paginationOpacity = 0;
}
doAjax();

    $(document).on("click","#delFile" ,function(){
        var tdVals = $(this).parent('td').siblings('td').map(function(i, td){
            return $(td).text();
        });
        console.log(tdVals);
        var row = $(this).closest("tr");
        var index = row.parent().children("tr").index(row);
        console.log(index);
        $.ajax({
            url: '/getfile/del',
            type: 'GET',
            contentType: 'application/json',
            data: ({
                fileName: tdVals[1]
            }),
            success: function (answer) {
//вывод страниц
                var path= answer.dataStr;
                console.log(path);

                location.reload(); // перезагружаем страницу

                //  var tr = $("#delFile");
               // var test = tr.parent().parent();
                //test.remove();
               // $(this).eq(index).remove();
               // var table = document.getElementById("tableList");
               // table.deleteRow(index+2);


                // console.log(test);

            }
        });
    });

    $(document).on("click","#editFile" ,function(){
        var tdVals = $(this).parent('td').siblings('td').map(function(i, td){
            return $(td).text();
        })

        $.ajax({
            url: '/getfile/edit',
            type: 'GET',
            contentType: 'application/json',
            data: ({
                fileName: tdVals[1]
            }),
            success: function (answer) {
//вывод страниц
                var path= answer.dataStr;
               window.location.href = path; //redirect to controller method which returns the jsp page

            }
        });
    });


    $(document).on("click",".pagination-page-number" ,function(){

        //Какое-то действие
        $(".pagination-page-number").removeClass("active");
        $(this).addClass("active");
        paginationLeftPos = $(this).prop("offsetLeft") + "px";
        paginationOpacity = 1;
        checkPaginationClick = 1;

        $(".pagination-hover-overlay").css({
            left: paginationLeftPos,
            backgroundColor: "#00178a",
            opacity: paginationOpacity });

        $(this).css({
            color: "#fff" });
    });


    $(document).on({
        mouseenter: function () {
            paginationOpacity = 1;
            $(".pagination-hover-overlay").css({
                backgroundColor: "#00c1dd",
                left: $(this).prop("offsetLeft") + "px",
                opacity: paginationOpacity });


            $(".pagination-page-number.active").css({
                color: "#333d45" });


            $(this).css({
                color: "#fff" });

        },

        mouseleave: function () {
            $(".pagination-hover-overlay").css({
                backgroundColor: "#00178a",
                opacity: paginationOpacity,
                left: paginationLeftPos });


            $(this).css({
                color: "#333d45" });


            $(".pagination-page-number.active").css({
                color: "#fff" });        }
    }, ".pagination-page-number"); //pass the element as an argument to .on


    function doAjax() {

        var inputText = "kek";

        $.ajax({
            url: '/getfile',
            type: 'GET',
            contentType: 'application/json',
            data: ({
                text: inputText
            }),
            success: function (answer) {
//вывод страниц
                var itDate = answer.data;
                console.log(itDate.length);
                console.log(itDate);
                //вывод таблицы
                $("#fileCount").empty();
                for (var i = 0; i < itDate.length; i++) {

                    var json =" <tr>\n" +
                        "                            <td>"+i+"</td>\n" +
                        "                            <td>"+itDate[i]+"</td>\n" +
                        "                            <td>\n" +
                        "                                <i id=\"editFile\" class=\"material-icons button-table edit\">edit</i>\n" +
                        "                                <i id=\"delFile\"class=\"material-icons button-table delete\">delete</i>\n" +
                        "                            </td>\n" +
                        "                        </tr>";

                    $('#fileCount').append(json);

                }
                $("#tableList").DataTable();

                //pageTest();
            }
        });
    }
/*
$(".pagination-page-number").click(function () {
  $(".pagination-page-number").removeClass("active");
  $(this).addClass("active");
  paginationLeftPos = $(this).prop("offsetLeft") + "px";
  paginationOpacity = 1;
  checkPaginationClick = 1;

  $(".pagination-hover-overlay").css({
    left: paginationLeftPos,
    backgroundColor: "#00178a",
    opacity: paginationOpacity });

  $(this).css({
    color: "#fff" });

});
*/
/*
$(".pagination-page-number").hover(
function () {
  paginationOpacity = 1;
  $(".pagination-hover-overlay").css({
    backgroundColor: "#00c1dd",
    left: $(this).prop("offsetLeft") + "px",
    opacity: paginationOpacity });


  $(".pagination-page-number.active").css({
    color: "#333d45" });


  $(this).css({
    color: "#fff" });

},
function () {
  $(".pagination-hover-overlay").css({
    backgroundColor: "#00178a",
    opacity: paginationOpacity,
    left: paginationLeftPos });
  
  
  $(this).css({
    color: "#333d45" });
  
  
  $(".pagination-page-number.active").css({
    color: "#fff" });
  
});*/
//# sourceURL=pen.js
});
}