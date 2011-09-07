(ns lazyspec.doc)

(defn resolve-fn-doc [f]
  (let [{:keys [ns name doc]} (meta f)]
    (if (and ns name)
              (:doc (meta (ns-resolve ns name)))
              doc)))

(defn add-fn-doc [f]
  (vary-meta f
             #(assoc % :doc (resolve-fn-doc f))))

(defn change-doc [obj f]
  (vary-meta obj #(let [doc (:doc %)]
                    (assoc % :doc (f doc)))))

(defn with-doc [f doc]
  (vary-meta f #(assoc % :doc doc)))
