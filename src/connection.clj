(ns connection
  (:import com.mchange.v2.c3p0.ComboPooledDataSource))

(def db-host "localhost")
(def db-port 5432)
(def db-name "juxt_db")

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname (str "//" db-host ":" db-port "/" db-name)
              :user "juxt"
              :password "juxt_secret"})

(defn pool
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec))
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               ;; expire excess connections after 30 minutes of inactivity:
               (.setMaxIdleTimeExcessConnections (* 30 60))
               ;; expire connections after 3 hours of inactivity:
               (.setMaxIdleTime (* 3 60 60)))]
    {:datasource cpds}))

(def pooled-db (delay (pool db-spec)))

;; TODO -- put inside a component
(defn db-connection [] @pooled-db)
