package cn.ffcs.shequ.utils.rules;

import java.util.stream.IntStream;

import cn.ffcs.shequ.security.rules.IRuleStrategy;

/**
 * 描述：身份证隐匿处理
 * Created by zengy on 2018/1/10.
 */
public class IdentityCardRule2 implements IRuleStrategy {
	
	// 身份证校验码
    private static final int[] COEFFICIENT_ARRAY = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    // 身份证号的尾数规则
    private static final String[] IDENTITY_MANTISSA = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
    private static final String IDENTITY_PATTERN = "^[0-9]{17}[0-9Xx]$";
	
    @Override
    public String operate(String txt) {
        if(txt!=null && txt.length()>1){
            String newtxt ="";
            if(txt.length()==18){
                newtxt = txt.replaceAll("(\\d{6})(\\d{8})([a-zA-Z\\d]{4})", "$1********$3");
            }else if(txt.length()==15){
                newtxt = txt.replaceAll("(\\d{6})(\\d{6})([a-zA-Z\\d]{3})", "$1******$3");
            }
            return newtxt;
        }
        return txt;
    }
    /**
     * 新版校验身份证号码
     * @author wuxq
     * @param identity
     * @return
     */
    
	public static boolean isLegalPattern(String identity) {
		if (identity == null) {
			return false;
		}

		if (identity.length() != 18) {
			return false;
		}

		if (!identity.matches(IDENTITY_PATTERN)) {
			return false;
		}

		char[] chars = identity.toCharArray();
		long sum = IntStream.range(0, 17).map(index -> {
			char ch = chars[index];
			int digit = Character.digit(ch, 10);
			int coefficient = COEFFICIENT_ARRAY[index];
			return digit * coefficient;
		}).summaryStatistics().getSum();

		// 计算出的尾数索引
		int mantissaIndex = (int) (sum % 11);
		String mantissa = IDENTITY_MANTISSA[mantissaIndex];

		String lastChar = identity.substring(17);
		if (lastChar.equalsIgnoreCase(mantissa)) {
			return true;
		} else {
			return false;
		}
	}
}

