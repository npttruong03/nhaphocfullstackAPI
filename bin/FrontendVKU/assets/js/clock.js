var countdownElement = document.getElementById("timer");
var countdownInterval;
var timeLeft;

window.addEventListener("beforeunload", function() {
  saveTimeLeft(timeLeft);
});

if (sessionStorage.getItem("timeLeft")) {
  timeLeft = parseInt(sessionStorage.getItem("timeLeft"));
} else {
  timeLeft = 3600;
}

startCountdown();

function startCountdown() {
  clearInterval(countdownInterval);
  countdownInterval = setInterval(updateCountdown, 1000);
  updateCountdown();
}

function updateCountdown() {
  var hours = Math.floor(timeLeft / 3600);
  var minutes = Math.floor((timeLeft % 3600) / 60);
  var seconds = timeLeft % 60;
  var formattedTime = padZero(hours) + ":" + padZero(minutes) + ":" + padZero(seconds);

  countdownElement.textContent = formattedTime;

  if (timeLeft <= 0) {
    clearInterval(countdownInterval);
    countdownElement.textContent = "00:00:00";
    timeLeft = 3600; // Reset lại giá trị timeLeft
    saveTimeLeft(timeLeft); // Lưu giá trị timeLeft
    window.location.replace("indexLog.html");
  } else {
    timeLeft--;
    saveTimeLeft(timeLeft); // Lưu giá trị timeLeft sau mỗi lần cập nhật
  }
}

function saveTimeLeft(time) {
  sessionStorage.setItem("timeLeft", time);
}

function padZero(number) {
  return (number < 10) ? "0" + number : number;
}