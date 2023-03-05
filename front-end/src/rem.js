const baseSize = 16;
// set rem
function setRem() {
  const scale = document.documentElement.clientWidth / 1920;
  document.documentElement.style.fontSize = `${baseSize * Math.min(scale, 3)}px`;
}
// init
setRem();
// reset rem when window size change
window.onresize = () => {
  setRem();
};
