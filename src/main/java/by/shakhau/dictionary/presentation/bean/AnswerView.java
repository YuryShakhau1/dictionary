package by.shakhau.dictionary.presentation.bean;

public class AnswerView {
	private Long correctId;
	private Long answerId;

	public AnswerView() {}

	public AnswerView(Long correctId, Long answerId) {
		super();
		this.correctId = correctId;
		this.answerId = answerId;
	}

	public Long getCorrectId() {
		return correctId;
	}

	public void setCorrectId(Long correctId) {
		this.correctId = correctId;
	}

	public Long getAnswerId() {
		return answerId;
	}

	public void setAnswerId(Long answerId) {
		this.answerId = answerId;
	}
}
