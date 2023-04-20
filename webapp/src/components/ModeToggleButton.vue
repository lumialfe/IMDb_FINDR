<template>
  <div id="app-color-changer" class="dark-mode">
    <div class="mode-toggle" @click="modeToggle">
      <div class="toggle">
        <div id="dark-mode" type="checkbox"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ModeToggleButton",
  data() {
    return {
      darkMode: true
    }
  },

  methods: {
    dark() {
      document.getElementById('app-color-changer').classList.add('dark-mode')
      document.documentElement.style.setProperty('--background-color', '#101010');
      document.documentElement.style.setProperty('--main-color', '#2f2f2f');
      document.documentElement.style.setProperty('--main-color-light', '#4f4f4f');
      document.documentElement.style.setProperty('--link-color', '#8f8f8f');
      document.documentElement.style.setProperty('--title-color', '#b6b6b6');
      this.darkMode = true
    },

    light() {
      document.getElementById("app-color-changer").classList.remove('dark-mode')
      document.documentElement.style.setProperty('--background-color', '#e3e3e3');
      document.documentElement.style.setProperty('--main-color', '#ababab');
      document.documentElement.style.setProperty('--main-color-light', '#646464');
      document.documentElement.style.setProperty('--link-color', '#1e1e1e');
      document.documentElement.style.setProperty('--title-color', '#000000');
      this.darkMode = false
    },

    modeToggle() {
      if (this.darkMode) {
        this.light()
      } else {
        this.dark()
      }
    },
  },

  computed: {
    darkDark() {
      return this.darkMode;
    }
  }
}
</script>

<style scoped lang="scss">
$dark: #171717;
$mode-toggle-bg: #262626;


// _mode-toggle.scss
.mode-toggle {
  position: relative;
  padding: 0;
  width: 44px;
  height: 24px;
  min-width: 36px;
  min-height: 20px;
  background-color: $mode-toggle-bg;
  border: 0;
  border-radius: 24px;
  outline: 0;
  overflow: hidden;
  cursor: pointer;
  z-index: 2;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  -webkit-touch-callout: none;
  appearance: none;
  transition: background-color .5s ease;

  .toggle {
    position: absolute;
    top: 0;
    left: 0;
    bottom: 0;
    margin: auto;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    border: 3px solid transparent;
    box-shadow: inset 0 0 0 2px #a5abba;
    overflow: hidden;
    transition: transform .5s ease;

    #dark-mode {
      position: relative;
      width: 100%;
      height: 100%;
      overflow: hidden;
      border-radius: 50%;

      &:before {
        content: '';
        position: relative;
        width: 100%;
        height: 100%;
        left: 50%;
        float: left;
        background-color: #a5abba;
        transition: border-radius .5s ease, width .5s ease, height .5s ease, left .5s ease, transform .5s ease;
      }
    }
  }
}

#app-color-changer.dark-mode {
  .mode-toggle {
    background-color: lighten($mode-toggle-bg, 5%);

    .toggle {
      transform: translateX(19px);

      #dark-mode {
        &:before {
          border-radius: 50%;
          width: 150%;
          height: 85%;
          left: 40%;
          transform: translate(-10%, -40%), rotate(-35deg);
        }
      }
    }
  }
}

#app-color-changer {
  position: fixed;
  top: 95%;
  left: 1%;
}
</style>