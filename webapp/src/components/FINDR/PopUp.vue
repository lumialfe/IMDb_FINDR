<template>
  <div class="popup">
    <button id="close">&times;</button>
    <h2>This Is The Title</h2>
    <p>
      Lorem, ipsum dolor sit amet consectetur adipisicing elit. Expedita distinctio fugiat alias iure qui, commodi
      minima magni ullam aliquam dignissimos?
    </p>
    <a href="#">Let's Go</a>
  </div>
</template>

<script>

export default {
  name: "PopUp",
  mounted() {
    window.addEventListener("load", function () {
      setTimeout(
          function open(event) {
            document.querySelector(".popup").style.display = "block";
          },
          1000
      )
    });
    document.querySelector("#close").addEventListener("click", function () {
      document.querySelector(".popup").style.display = "none";
    });
  }
}
</script>

<style scoped lang="scss">
@import "../../assets/styles/styles.scss";
.popup {
  background-color: $main-color;
  width: 450px;
  padding: 30px 40px;
  position: absolute;
  transform: translate(-50%, -50%);
  right: 0;
  top: 200px;
  border-radius: 8px;
  display: none;
  text-align: center;
}

.popup button {
  display: block;
  margin: 0 0 20px auto;
  background-color: transparent;
  font-size: 30px;
  color: #c5c5c5;
  border: none;
  outline: none;
  cursor: pointer;
}

.popup p {
  font-size: 14px;
  text-align: justify;
  margin: 20px 0;
  line-height: 25px;
}

a {
  display: block;
  width: 150px;
  position: relative;
  margin: 10px auto;
  text-align: center;
  background-color: #0f72e5;
  color: #ffffff;
  text-decoration: none;
  padding: 5px 0;
}
</style>