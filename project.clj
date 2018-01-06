(defproject impi "0.0.11-SNAPSHOT"
  :description "ClojureScript library for using Pixi.js through immutable data"
  :url "https://github.com/weavejester/impi"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946"]
                 [cljsjs/pixi "4.6.2-0"]]
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel "0.5.14"]]
  :cljsbuild
  {:builds
   {:main
    {:figwheel     true
     :source-paths ["src"]
     :compiler     {:main          impi.core
                    :asset-path    "js/out"
                    :output-to     "resources/public/js/main.js"
                    :output-dir    "resources/public/js/out"
                    :optimizations :none}}}}
  :figwheel
  {:http-server-root "public"
   :server-port      3001
   :css-dirs         ["resources/public/css"]}
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.14"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :repl-options {:init             (set! *print-length* 50)
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
