window.onload=function(){
$(document).ready(function(e) {   
  // your code here


    $(document).on("click","#boost" ,function(){

            var $item = $(this).closest("tr")   // Finds the closest row <tr>
                .find("#fileTxt")     // Gets a descendent with class="nr"
                .text();         // Retrieves the text within <td>

        var last = $(this).closest("tr")
            .find("#rate")
            .text();

        var test = $(this);

        $.ajax({
              url: '/changeRating',
              type: 'GET',
              contentType: 'application/json',
              data: ({
                  fileName: $item
              }),
              success: function (answer) {
                  var path= answer;

                  var value = parseFloat(last.replace(",", "."));
                  value = value+0.25;
                  console.log("Значение +");
                  console.log(value);
                  test.closest("tr")
                      .find("#rate")
                      .text(value);
              }
          });
    });


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