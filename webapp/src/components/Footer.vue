<template>
  <footer>
    <div class="footer--content">
      <div class="footer--content--divider">
        <a target="_blank" href="https://github.com/lumialfe/IMDb_FINDR">GitHub Repository</a>
        <a target="_blank" href="https://www.imdb.com/">IMDb</a>
      </div>
      <div class="footer--content--divider">
        <a target="_blank" href="https://empathy.co">Empathy.co</a>
        <a target="_blank" href="https://empathy.co/company/academy/">Academy Program</a>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "Footer"
}
</script>

<style scoped lang="scss">
@import "../assets/styles/styles.scss";

footer {
  grid-area: footer;
  background-color: $main-color;
  margin-top: 2rem;
  width: 100%;

  position: relative;
  bottom: 0;

  .footer--content {
    display: grid;
    grid-template-columns: 50% 50%;
    width: 60%;
    margin: auto;
    text-align: center;

    .footer--content--divider {
      padding-left: 3rem;
      margin: 3rem 0;
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      text-align: left;
      border-left: 3px solid $link-color;
      gap: .6rem;

      a {
        &:hover {
          text-decoration: underline;
        }
      }
    }
  }
}

@media only screen and (max-width: 640px) {
  footer {
    width: 100%;

    .footer--content {
      width: 100%;
    }
  }
}
</style>