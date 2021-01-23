window.onscroll = function () { getSticky() };
window.onload = function () {

  n = new Date();
  y = n.getFullYear();
  m = n.getMonth() + 1;
  d = n.getDate();
  document.getElementById("date").innerHTML = d + "/" + m + "/" + y;
}

function getSticky() {

  var titleBar = document.getElementById("title-bar");

  var statusBox = document.getElementById("status");

  var indicatorBar = document.getElementById("indicator");

  var sticky = titleBar.offsetTop;

  if (window.pageYOffset >= sticky) {
    titleBar.classList.add("sticky_");
    indicatorBar.classList.add("hhh");
  } else {
    titleBar.classList.remove("sticky_");
  }
}