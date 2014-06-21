(ns migrations
  (:require [clojure.java.jdbc :as jdbc]
            [connection :refer [db-connection]]))

;; TODO -- move to drift

(defn create-tables []
  (jdbc/db-do-commands (db-connection)
                       (jdbc/create-table-ddl :company
                                              [:id "serial primary key"]
                                              [:name "varchar(32)"])
                       (jdbc/create-table-ddl :branch
                                              [:id "serial primary key"]
                                              [:name "varchar(32)"]
                                              [:branch_id "varchar(32)"]
                                              [:company "integer references company(id)"])
                       (jdbc/create-table-ddl :site_user
                                              [:id "serial primary key"]
                                              [:name "varchar(32)"]
                                              [:password "varchar(64)"]
                                              [:company "integer references company(id)"])
                       (jdbc/create-table-ddl :site_user_branch
                                              [:id "serial primary key"]
                                              [:site_user "integer references site_user(id)"]
                                              [:branch "integer references branch(id)"]))
  (jdbc/db-do-commands (db-connection)
                       "CREATE INDEX branch_name_idx ON branch (name)"
                       "CREATE INDEX branch_id_idx ON branch (id)"))

(defn drop-tables []
  (jdbc/db-do-commands (db-connection)
                       (jdbc/drop-table-ddl :site_user_branch)
                       (jdbc/drop-table-ddl :site_user)
                       (jdbc/drop-table-ddl :branch)
                       (jdbc/drop-table-ddl :company)
                       ))

(comment
  (drop-tables)
  (create-tables)
  )
