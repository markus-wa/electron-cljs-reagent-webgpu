(ns app.renderer.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]
            [cljs.core.async :refer [go]]
            [cljs.core.async.interop :refer-macros [<p!]])
  (:require-macros [app.utils.macros :refer [inline-resource]]))

(def fragment-shader-src (inline-resource "shaders/fragment.wgsl"))
(def vertex-shader-src (inline-resource "shaders/vertex.wgsl"))

(enable-console-print!)

(def swap-chain-format "bgra8unorm")

(defn device [adapter] ^js/Promise
  (.requestDevice adapter))

(defn ->pipeline [^js/GPUDevice device swap-chain-format]
  (.createRenderPipeline device
                         (clj->js
                          {:vertex
                           {:module
                            (.createShaderModule device #js{:code vertex-shader-src})
                            :entryPoint "main"}
                           :fragment
                           {:module
                            (.createShaderModule device #js{:code fragment-shader-src})
                            :entryPoint "main"
                            :targets [{:format swap-chain-format}]}
                           :primitive
                           {:topology "triangle-list"}})))

(defn command-encoder [device] ^js/CommandEncoder
  (.createCommandEncoder device))

(defn start-webgpu
  [^js/HTMLCanvasElement canvas]
  ;; TODO: auto-resize smoothly
  (go
    (if (not (. js/navigator -gpu))
      (js/alert "Your browser does not support WebGPU or it is not enabled. More info: https://webgpu.io")
      (let [adapter ^js/GPUAdapter (<p! (.requestAdapter (. js/navigator -gpu)))
            device ^js/GPUDevice (<p! (device adapter))
            context (doto (.getContext canvas "webgpu")
                      (.configure
                       (clj->js
                        {:device device
                         :format swap-chain-format})))
            pipeline (->pipeline device swap-chain-format)
            command-enc (command-encoder device)
            texture-view (-> context .getCurrentTexture .createView)
            render-pass-desc {:colorAttachments
                              [{:view texture-view
                                :loadValue {:r 1.0 :g 1.0 :b 1.0 :a 1.0}}]}
            pass-enc (.beginRenderPass command-enc (clj->js render-pass-desc))]
        (doto pass-enc
          (.setPipeline pipeline)
          (.draw 3 1 0 0)
          (.endPass))
        (.submit (. device -queue) (clj->js [(.finish command-enc)]))))))

(defn output []
  (r/create-class
   {:display-name "output"
    :component-did-mount #(start-webgpu (rd/dom-node %))
    :reagent-render
    (fn []
      [:canvas {:id "output"
                :style {:flex-grow "1"}}])}))

(def output2
  (with-meta output
    {:component-did-mount #(start-webgpu (rd/dom-node %))}))

(defn root-component
  []
  [:div {:style {:height "100%"
                 :display "flex"
                 :flex-direction "column"}}
   [output]])

(defn ^:dev/after-load start! []
  (rd/render
   [root-component]
   (js/document.getElementById "app-container")))
