# avtron

Audio-Visual things with ClojureScript + Shadow-cljs + Electron + Reagent + GLSL + SPIR-V

GLSL & SPIR-V still TODO

## How to Run
```
yarn global add electron-nightly
yarn global add shadow-cljs
yarn

yarn run dev
electron --enable-unsafe-webgpu --enable-features=Vulkan .
```

## Release
```
yarn run build
electron-packager . HelloWorld --platform=darwin --arch=x64 --version=1.4.13
```

## Acknowledgements

- [ahonn/shadow-electron-starter](https://github.com/ahonn/shadow-electron-starter)
