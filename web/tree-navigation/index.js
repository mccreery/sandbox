function clamp(min, max, x) {
  return x < min ? min : x > max ? max : x;
}

function lerp(a, b, t) {
  return a * (1 - t) + b * t;
}

function unlerp(a, b, x) {
  return (x - a) / (b - a);
}

function setClass(element, className, enabled) {
  if (enabled) {
    element.classList.add(className);
  } else {
    element.classList.remove(className);
  }
}

class SidePanel {
  constructor(panelElement, shadowElement, transitionDuration) {
    this.panelElement = panelElement;
    this.shadowElement = shadowElement;
    this._rootElement = panelElement.ownerDocument.documentElement;

    this.transitionDuration = transitionDuration;
    this._openFraction = 0;
  }

  get openFraction() {
    return this._openFraction;
  }

  set openFraction(openFraction) {
    this._openFraction = clamp(0, 1, openFraction);

    this.panelElement.style.transform = `translateX(${(this._openFraction - 1) * 100}%)`;
    this.shadowElement.style.opacity = this._openFraction;
    setClass(this._rootElement, "nav-open", this._openFraction > 0);
  }

  get clientX() {
    return lerp(-this.panelElement.offsetWidth, 0, this._openFraction);
  }

  set clientX(clientX) {
    this.openFraction = unlerp(-this.panelElement.offsetWidth, 0, clientX);
  }

  grab() {
    this.clientX = this.panelElement.getBoundingClientRect().x;

    this.panelElement.style.transitionDuration = "";
    this.shadowElement.style.transitionDuration = "";
  }

  release(openFraction) {
    let duration = Math.abs(openFraction - this._openFraction) * this.transitionDuration + "s";
    this.openFraction = openFraction;

    this.panelElement.style.transitionDuration = duration;
    this.shadowElement.style.transitionDuration = duration;
  }
}

const sidePanel = new SidePanel(document.getElementById("nav"), document.getElementById("nav-shadow"), 0.4);

const gestures = {
  DEFAULT: 0,
  SWIPE: 1,
  SCROLL: 2
};

function getHorizScrollAncestor(element) {
  while (element !== null && element.scrollWidth <= element.clientWidth) {
    element = element.parentElement;
  }
  return element;
}

let touchX;
let touchY;
let gesture;
let horizScrollAncestor;

document.addEventListener("touchstart", e => {
  if (e.touches.length > 1) {
    release(e);
    gesture = gestures.SCROLL;
  } else {
    touchX = e.changedTouches[0].clientX;
    touchY = e.changedTouches[0].clientY;
    gesture = gestures.DEFAULT;
    horizScrollAncestor = getHorizScrollAncestor(e.target);
  }
});

const minGestureDistance = 20;

let slideTime;
let slideX;
let slideOffsetX;

const directions = {
  LEFT: 0,
  RIGHT: 1
};

function canScrollHoriz(element, direction) {
  if (element === null) {
    return false;
  } else if (direction === directions.LEFT) {
    return element.scrollLeft > 0;
  } else if (direction === directions.RIGHT) {
    let scrollLeftMax = element.scrollWidth - element.clientWidth;
    return element.scrollLeft < scrollLeftMax;
  }
}

document.addEventListener("touchmove", e => {
  let touch = e.changedTouches[0];

  if (gesture === gestures.DEFAULT) {
    let dx = touch.clientX - touchX;
    let absDx = Math.abs(dx);
    let absDy = Math.abs(touch.clientY - touchY);

    if (absDx >= minGestureDistance || absDy >= minGestureDistance) {
      if (absDy >= absDx || canScrollHoriz(horizScrollAncestor, dx > 0 ? directions.LEFT : directions.RIGHT)) {
        gesture = gestures.SCROLL;
      } else {
        gesture = gestures.SWIPE;

        sidePanel.grab();
        slideTime = e.timeStamp;
        slideX = touch.clientX;
        slideOffsetX = slideX - sidePanel.clientX;
      }
    }

    if (gesture !== gestures.DEFAULT) {
      console.log(Object.keys(gestures).find(key => gestures[key] === gesture));
    }
  }

  if (gesture === gestures.SWIPE) {
    e.preventDefault();
    sidePanel.clientX = touch.clientX - slideOffsetX;
  }
}, { passive: false });

function release(e) {
  if (gesture !== gestures.SWIPE) {
    return;
  }

  let velocity = (e.changedTouches[0].clientX - slideX) / (e.timeStamp - slideTime);

  let openFraction;

  const minVelocity = 0.5;
  if (velocity <= -minVelocity) {
    openFraction = 0;
  } else if (velocity >= minVelocity) {
    openFraction = 1;
  } else if (sidePanel.openFraction < 0.5) {
    openFraction = 0;
  } else {
    openFraction = 1;
  }

  sidePanel.release(openFraction);
}

document.addEventListener("touchend", e => {
  release(e);
});

sidePanel.shadowElement.addEventListener("click", () => {
  sidePanel.release(0);
});

document.getElementById("menu-button").addEventListener("click", () => {
  sidePanel.release(1);
});
