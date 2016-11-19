(set-env!
  :source-paths   #{"src"}
  :dependencies   '[; dev
                    [adzerk/bootlaces      "0.1.13" :scope "test"]
                    [alda/core             "0.1.0"  :scope "test"]
                    [alda/sound-engine-clj "0.1.0"  :scope "test"]

                    ; dependencies
                    [com.taoensso/timbre   "4.1.1"]
                    [io.aviso/pretty       "0.1.20"]
                    [jline                 "2.12.1"]])

(require '[adzerk.bootlaces :refer :all])

(def ^:const +version+ "0.1.0")

(bootlaces! +version+)

(task-options!
  pom     {:project 'alda/repl-clj
           :version +version+
           :description "A Clojure implementation of an Alda REPL"
           :url "https://github.com/alda-lang/alda-repl-clj"
           :scm {:url "https://github.com/alda-lang/alda-repl-clj"}
           :license {"name" "Eclipse Public License"
                     "url" "http://www.eclipse.org/legal/epl-v10.html"}}

  jar     {:file "alda-repl-clj.jar"}

  install {:pom "alda/repl-clj"}

  target  {:dir #{"target"}})

(deftask dev
  "Runs the Alda REPL for development."
  []
  (comp
    (with-pass-thru fs
      (require 'alda.repl)
      ((resolve 'alda.repl/start-repl!)))
    (wait)))

(deftask package
  "Builds jar file."
  []
  (comp (pom)
        (jar)))

(deftask deploy
  "Builds jar file, installs it to local Maven repo, and deploys it to Clojars."
  []
  (comp (package) (install) (push-release)))

