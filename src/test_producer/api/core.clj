(ns test-producer.api.core
  (:require [io.pedestal.http :as server]
            [muuntaja.core :as m]
            [reitit.coercion.schema]
            [reitit.dev.pretty :as pretty]
            [reitit.http :as http]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.pedestal :as pedestal]
            [reitit.ring :as ring]
            [test-producer.api.routes :as routes])
  (:import (java.net DatagramSocket InetAddress)))


(defn- create-router []
  (pedestal/routing-interceptor
    (http/router
      routes/routes
      {:exception pretty/exception
       :data      {:coercion     reitit.coercion.schema/coercion
                   :muuntaja     m/instance
                   :interceptors [;; query-params & form-params
                                  (parameters/parameters-interceptor)
                                  ;; content-negotiation
                                  (muuntaja/format-negotiate-interceptor)
                                  ;; encoding response body
                                  (muuntaja/format-response-interceptor)
                                  ;; exception handling
                                  (exception/exception-interceptor)
                                  ;; decoding request body
                                  (muuntaja/format-request-interceptor)]}})
    ;; optional default ring handlers (if no routes have matched)
    (ring/routes (ring/create-default-handler))))


(defn- get-host-ip [{:keys [host port]}]
  (try
    (-> (doto (DatagramSocket.)
          (.connect (InetAddress/getByName host) port))
        (.getLocalAddress)
        (.getHostAddress))
    (catch Exception e
      (println (format "Failed to get host ip: %s: %s" (.getName (class e)) e)))))


(defn start [{:keys [host port]}]
  (let [server (-> {::server/type   :jetty
                    ::server/host   host
                    ::server/port   port
                    ::server/join?  false
                    ;; no pedestal routes
                    ::server/routes []}
                   (server/default-interceptors)
                   ;; use the reitit router
                   (pedestal/replace-last-interceptor (create-router))
                   #_(server/dev-interceptors)
                   (server/create-server))]
    (server/start server)
    (println (format "Started HTTP server on %s:%s"
                     (or (when (= "0.0.0.0" host)
                           (get-host-ip {:host "8.8.8.8" :port 80}))
                         host)
                     port))
    server))


(defn stop [server]
  (println "Stopping HTTP server")
  (server/stop server))
