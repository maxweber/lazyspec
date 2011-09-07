(ns lazyspec.core
  (:use lazytest.suite
        lazytest.test-case
        lazyspec.doc))

(defn- clasp [name f]
  (fn [doc & fns]
    (suite (fn []
             (test-seq
              (with-meta
                (map test-case (f fns))
                {:doc (str name ": " doc)}))))))

(def Scenario (clasp "Scenario" #(apply concat %)))

(def Feature (clasp "Feature" identity))

(defn- block [name]
  (fn [& fns]
    (let [fns (map add-fn-doc fns)
          [first & rest] fns
          first (change-doc first #(str name " " %))
          rest (map (fn [f] (change-doc f #(str "And " %))) rest)
          fns (cons first rest)]
      fns)))

(def Given (block "Given"))

(def When (block "When"))

(def Then (block "Then"))
