window.onscroll = function() {getSticky()};
window.onload=function(){
	
	n =  new Date();
y = n.getFullYear();
m = n.getMonth() + 1;
d = n.getDate();
document.getElementById("date").innerHTML = d + "/" + m + "/" + y;
}


function getSticky() {
	
	var titleBar = document.getElementById("title-bar");
	
	var statusBox = document.getElementById("status");
	var sticky = titleBar.offsetTop;
	var statusSticky = statusBox.offsetTop;
	
  if (window.pageYOffset >= sticky) {
    titleBar.classList.add("sticky")
  } else {
    titleBar.classList.remove("sticky");
  }
  if (window.pageYOffset >= 100) {
    statusBox.classList.add("stickyside")
  } else {
    statusBox.classList.remove("stickyside");
  }
}

