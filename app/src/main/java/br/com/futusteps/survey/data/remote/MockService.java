package br.com.futusteps.survey.data.remote;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import br.com.futusteps.survey.core.surveranswer.SurveyAnswer;
import br.com.futusteps.survey.core.survey.Survey;

public interface MockService {

    String MOCK_RESP = "[\n" +
            "   {\n" +
            "      \"id\":1235,\n" +
            "      \"name\":\"Pesquisa Mercado Padrão\",\n" +
            "      \"questions\":[\n" +
            "         {\n" +
            "            \"id\":1,\n" +
            "            \"order\":\"1\",\n" +
            "            \"description\":\"Qual foi a última vez que você usou seu cartão de credito?\",\n" +
            "            \"type\":\"text\"\n" +
            "         },\n" +
            "         {\n" +
            "            \"id\":2,\n" +
            "            \"order\":\"2\",\n" +
            "            \"description\":\"Você já fez alguma compra com seu cartão essa semana?\",\n" +
            "            \"type\":\"single\",\n" +
            "            \"alternatives\":[\n" +
            "               {\n" +
            "                  \"id\":1,\n" +
            "                  \"description\":\"Sim\"\n" +
            "               },\n" +
            "               {\n" +
            "                  \"id\":2,\n" +
            "                  \"description\":\"Não\"\n" +
            "               }\n" +
            "            ]\n" +
            "         }\n" +
            "      ]\n" +
            "   },\n" +
            "   {\n" +
            "      \"id\":12356,\n" +
            "      \"name\":\"Pesquisa Mercado Laranjão\",\n" +
            "      \"questions\":[\n" +
            "         {\n" +
            "            \"id\":1,\n" +
            "            \"order\":\"1\",\n" +
            "            \"description\":\"Qual foi a última vez que você usou seu cartão de credito?\",\n" +
            "            \"type\":\"text\"\n" +
            "         },\n" +
            "         {\n" +
            "            \"id\":2,\n" +
            "            \"order\":\"2\",\n" +
            "            \"description\":\"Você já fez alguma compra com seu cartão essa semana?\",\n" +
            "            \"type\":\"single\",\n" +
            "            \"alternatives\":[\n" +
            "               {\n" +
            "                  \"id\":1,\n" +
            "                  \"description\":\"Sim\"\n" +
            "               },\n" +
            "               {\n" +
            "                  \"id\":2,\n" +
            "                  \"description\":\"Não\"\n" +
            "               }\n" +
            "            ]\n" +
            "         }\n" +
            "      ]\n" +
            "   }\n" +
            "]";

    List<Survey> getSurveys(String user, String token);

    void saveSurvey(SurveyAnswer surveyAnswer);

    class Builder {


        public static MockService build() {
            return new MockService() {
                @Override
                public List<Survey> getSurveys(String user, String token) {
                    Type listType = new TypeToken<List<Survey>>() {
                    }.getType();
                    return new Gson().fromJson(MOCK_RESP, listType);
                }

                @Override
                public void saveSurvey(SurveyAnswer surveyAnswer) {

                }

            };
        }
    }


}
