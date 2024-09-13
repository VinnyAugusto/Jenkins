def getJsonFromRestApi (apiUrl, responseFile)
{
    def responseContent = ''
    def statusCode = ''
    
    try {
        def curlResp = bat(script: """ curl -s -k -X GET "${apiUrl}" -o ${responseFile} -w \"%%{http_code}\" """ , returnStdout: true).trim()
    
        echo "Retorno do Curl: ${curlResp}"
    
        statusCode = curlResp.split('\n')[1].trim();
        
        echo "Status Code obtido da API REST: ${statusCode}"
        
        responseContent = readFile(responseFile)
        echo "Json obtido da API REST: ${responseContent}"
        
        if(statusCode == "200")
        {
            def jsonSlurper = new groovy.json.JsonSlurper()
            def responseJson = jsonSlurper.parseText(responseContent)
            
            return responseJson;
        }
    }
    catch (Exception e)
    {
        def msg = '';
        
        if( e.getMessage().trim() == "script returned exit code 7")
            msg = "Erro retornado indica que a API estava offline"
        else
            msg = "Erro retornado: ${e.getMessage()}"
        
        echo(msg)
        error(msg)
    }
    
    error ("erro ao chamar a API. -> ${statusCode} : ${responseContent}")
}
