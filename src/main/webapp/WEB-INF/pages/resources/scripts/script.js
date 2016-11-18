$(document).idle({
	onIdle: function(){
		window.location.href = "/pin";
	},
	idle: 10*1000
})