package cn.ffcs.zhsq.utils;

public enum AqiState {
	优("1"), 
	良("2"),
	轻度污染("3"),
	中度污染("4"),
	重度污染("5"),
	严重污染("6"),
	轻度("7"),
	中度("8"),
	重度("9"),
	严重("10");
	private String index;
	// 构造方法
	private AqiState(String index) {
		this.index = index;
	}

	// get set 方法

	public String getIndex() {
		return index;
	}

}
