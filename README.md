# avtron

Audio-Visual things with ClojureScript + Shadow-cljs + Electron + Reagent + GLSL + SPIR-V

(GLSL & SPIR-V still TODO)

![image](https://user-images.githubusercontent.com/5138316/145599898-b344fa90-d988-45c8-837a-421e6ef176d9.png)

Only tested on Ubuntu 20.04 so far, some instructions missing - please get in touch if you're facing problems running this.

## How to Run
```
yarn

yarn run dev
yarn run electron # in another terminal
```

## Release
```
yarn run build
electron-packager . HelloWorld --platform=darwin --arch=x64 --version=1.4.13
```

## Acknowledgements

- [ahonn/shadow-electron-starter](https://github.com/ahonn/shadow-electron-starter)
- [James226/webgpu-electron](https://github.com/James226/webgpu-electron)
