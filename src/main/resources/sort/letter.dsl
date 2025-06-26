{
    "_script": {
        "type": "string",
        "script": {
            "lang": "painless",
            "source": "if(params._source.#(params.field) !=null){params._source.#(params.field).toLowerCase()} else {''}"
        },
        "order": "#(params.sortBy)"
    }
}