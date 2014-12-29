(ns hello-ring.core
  (:gen-class)
  (:require [ring.adapter.jetty]
            [ring.middleware.stacktrace]
            [clojure.pprint]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "<h1>Hello World!</h1>"})

(defn wrap-spy [handler]
  (fn [request]
    (println "-------------------------------")
    (println "Incoming Request:")
    (clojure.pprint/pprint request)
    (let [response (handler request)]
      (println "Outgoing Response Map:")
      (clojure.pprint/pprint response)
      (println "-------------------------------")
      response)))

;; app is just a function that takes a request map, and returns a response map
(def app
  (-> #'handler ;; passing functions as a Var, allows us to change the function dynamically 
      (ring.middleware.stacktrace/wrap-stacktrace) ;; send back exception in HTML
      ;(wrap-spy)
      ))

(defonce server (ring.adapter.jetty/run-jetty #'app {:port 8080 :join? false}))

