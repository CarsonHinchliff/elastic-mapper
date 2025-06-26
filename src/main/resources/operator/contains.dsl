{
    "bool" : {
        "should": [
            {
                "bool": {
                    "must": [
                        #for(value: params.value.split("[\s-]"))
                            {
                                "wildcard": {
                                    "#(params.field)": {
                                        "value": "*#(value)*"
                                    }
                                }
                            }
                            #if(!for.last)
                            ,
                            #end
                        #end
                    ]
                }
            }
            #if("text".equals(params.type))
            ,
            {
                "wildcard": {
                    "#(params.field).keyword": {
                        "value": "*#(params.value)*"
                    }
                }
            }
            #end
        ],
        "minimum_should_match": 1
    }
}