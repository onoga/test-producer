(defproject test-producer "1.0.0"
  :description "Kafka test producer"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [mount "0.1.16"]
                 [net.tbt-post/clj-kafka-x "0.3.1"]
                 [net.tbt-post/zlib-tiny "0.2.3"]
                 [cheshire "5.8.1"]
                 [net.tbt-post/zlib-tiny "0.2.3"]

                 ;; HTTP
                 [io.pedestal/pedestal.service "0.5.7"]
                 [io.pedestal/pedestal.jetty "0.5.7"]
                 [metosin/reitit "0.4.2"]
                 [metosin/reitit-pedestal "0.4.2"]]

  :main ^:skip-aot test-producer.core
  :omit-source true

  :target-path "target/%s"
  :clean-targets ^{:protect false} [:target-path]

  :jar-name "test-producer-current.jar"
  :uberjar-name "test-producer-current-standalone.jar"

  :profiles {:uberjar {:aot :all}}

  :plugins [[lein-ancient "0.6.15"]]

  :repositories [["private-jars" "http://local.repo:9180/repo"]])

(cemerick.pomegranate.aether/register-wagon-factory!
  "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))
