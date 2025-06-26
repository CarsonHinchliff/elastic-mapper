{
    "nested": {
        "path": "#(nestedPath)",
        "query": {
            "bool": {
                "must": [
                    #(filter)
                ]
            }
        }
        #if(enableInnerHits)
        ,
        "inner_hits": {
            "size": "100"
        }
        #end
    }
}