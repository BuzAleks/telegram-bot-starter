package link.buzalex.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BotTextExpressionEvaluator {
    private static final Logger LOG = LoggerFactory.getLogger(BotTextExpressionEvaluator.class);

    private final ExpressionParser parser = new SpelExpressionParser();
    private final StandardEvaluationContext parserContext = new StandardEvaluationContext();
    private final String open = "#{";
    private final String close = "}";

    public String evaluate(String text, Map<String, Object> context) {
        parserContext.setVariables(context);
        String formattedText = text;
        String[] strings = StringUtils.substringsBetween(text, open, close);
        if (strings != null) {
            for (String s : strings) {
                try {
                    Expression expression = parser.parseExpression(s);
                    Object value = expression.getValue(parserContext);
                    if (value != null) {
                        formattedText = StringUtils.replace(formattedText, open + s + close, value.toString());
                    }
                } catch (ParseException | EvaluationException e) {
                    LOG.error("Evaluation error", e);
                }
            }
        }
        return formattedText;
    }
}
