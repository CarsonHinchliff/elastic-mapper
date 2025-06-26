{
    "range": {
        "#(params.field)": {
            "gte": "#(params.multipleValue[0])"
            #if(params.multipleValue.length > 1)
            ,
            "lt": "#(params.multipleValue[1])"
            #end
        }
    }
}