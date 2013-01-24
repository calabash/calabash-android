package sh.calaba.instrumentationbackend.query.ast;

public enum ComparisonOperator implements UIQueryASTPredicateRelation {

	LESSTHAN {
		public boolean areRelated(Object firstValue, Object secondValue) {
			if (areNumbers(firstValue, secondValue)) {
				Number firstNum = (Number) firstValue;
				Number secondNum = (Number) secondValue;
				return firstNum.doubleValue() < secondNum.doubleValue();
			} else {
				return false;
			}
		}
	},

	LESSTHANOREQUAL {
		public boolean areRelated(Object firstValue, Object secondValue) {
			return EQUAL.areRelated(firstValue, secondValue) || LESSTHAN.areRelated(firstValue, secondValue);
		}
	},

	EQUAL {
		public boolean areRelated(Object firstValue, Object secondValue) {
			if (firstValue == secondValue) {
				return true;
			}
			return (firstValue != null) && firstValue.equals(secondValue);					
		}
	},

	GREATERTHAN {
		public boolean areRelated(Object firstValue, Object secondValue) {
			if (areNumbers(firstValue, secondValue)) {
				Number firstNum = (Number) firstValue;
				Number secondNum = (Number) secondValue;
				return firstNum.doubleValue() > secondNum.doubleValue();
			} else {
				return false;
			}
		}
	},

	GREATERTHANOREQUAL {
		public boolean areRelated(Object firstValue, Object secondValue) {
			return EQUAL.areRelated(firstValue, secondValue) || GREATERTHAN.areRelated(firstValue, secondValue);
		}
	};

	protected boolean areNumbers(Object firstValue, Object secondValue) {
		return (firstValue instanceof Number && secondValue instanceof Number);
	}

}
