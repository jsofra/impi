(ns example.core
  (:require [impi.core :as impi]
            [devtools.core :as devtools]))

(enable-console-print!)
(devtools/install!)

(defonce renderer
  (impi/renderer [400 300] {:background-color 0xbbbbbb}))

(defonce state
  (atom {}))

(def outline-shader
  "precision mediump float;

   varying vec2 vTextureCoord;
   uniform sampler2D uSampler;
   uniform vec2 dimensions;

   void main(void) {
       vec2 pixelSize  = vec2(4.0) / dimensions;
       vec4 pixel      = texture2D(uSampler, vTextureCoord);
       vec4 pixelUp    = texture2D(uSampler, vTextureCoord - vec2(0.0, pixelSize.y));
       vec4 pixelDown  = texture2D(uSampler, vTextureCoord + vec2(0.0, pixelSize.y));
       vec4 pixelLeft  = texture2D(uSampler, vTextureCoord - vec2(pixelSize.x, 0.0));
       vec4 pixelRight = texture2D(uSampler, vTextureCoord + vec2(pixelSize.x, 0.0));

       if (pixel.a == 0.0 && (pixelUp.a    > 0.0 ||
                              pixelDown.a  > 0.0 ||
                              pixelLeft.a  > 0.0 ||
                              pixelRight.a > 0.0)) {
           pixel = vec4(1.0, 0.0, 0.0, 1.0);
       }
       else {
           pixel = vec4(0.0, 0.0, 0.0, 0.0);
       }

       gl_FragColor = pixel;
   }")

(reset! state
        {:impi/key  :stage
         :pixi/type :pixi.type/container
         :pixi.container/children
         [{:impi/key  :bunny1
           :pixi/type :pixi.type/sprite
           :pixi.object/position [200 150]
           :pixi.object/rotation 0.0
           :pixi.sprite/anchor   [0.5 0.5]
           :pixi.sprite/texture
           {:pixi/type               :pixi.type/texture
            :pixi.texture/scale-mode :pixi.texture.scale-mode/linear
            :pixi.texture/source     "img/bunny.png"}}
          {:impi/key  :bunny2
           :pixi/type :pixi.type/sprite
           :pixi.object/position [100 100]
           :pixi.object/scale    [5 5]
           :pixi.object/interactive? true
           :pixi.event/mouse-down    #(prn :clicked)
           :pixi.sprite/anchor   [0.5 0.5]
           :pixi.sprite/texture
           {:pixi/type               :pixi.type/texture
            :pixi.texture/scale-mode :pixi.texture.scale-mode/nearest
            :pixi.texture/source     "img/bunny.png"}}
          {:impi/key  :rendered
           :pixi/type :pixi.type/sprite
           :pixi.object/position [0 0]
           :pixi.object/scale [3 3]
           :pixi.object/alpha 0.8
           :pixi.sprite/texture
           {:pixi/type               :pixi.type/render-texture
            :pixi.texture/scale-mode :pixi.texture.scale-mode/nearest
            :pixi.render-texture/size [100 100]
            :pixi.render-texture/source
            {:impi/key  :bunny3
             :pixi/type :pixi.type/sprite
             :pixi.object/position [50 50]
             :pixi.sprite/anchor   [0.5 0.5]
             :pixi.sprite/texture
             {:pixi/type               :pixi.type/texture
              :pixi.texture/scale-mode :pixi.texture.scale-mode/nearest
              :pixi.texture/source     "img/bunny.png"}
             :pixi.object/filters
             [{:pixi.filter/fragment outline-shader
               :pixi.filter/uniforms {:dimensions {:type "2f" :value [400.0 300.0]}}}]}}}]})

(defn animate [state]
  (swap! state update-in [:pixi.container/children 0 :pixi.object/rotation] + 0.1)
  (js/setTimeout #(animate state) 16))

(let [element (.getElementById js/document "app")]
  (impi/mount renderer element)
  (impi/render renderer @state)
  (add-watch state ::render (fn [_ _ _ s] (impi/render renderer s))))

(defonce x
  (animate state))
