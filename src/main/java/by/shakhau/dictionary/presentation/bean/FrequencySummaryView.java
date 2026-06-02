package by.shakhau.dictionary.presentation.bean;

public class FrequencySummaryView {
	private long doNotKnowSize;
	private long alreadyKnowSize;
	private long almostKnowSize;
	private long knowSize;
	private long unclassifiedSize;

	public long getDoNotKnowSize() {
		return doNotKnowSize;
	}

	public void setDoNotKnowSize(long doNotKnowSize) {
		this.doNotKnowSize = doNotKnowSize;
	}

	public long getAlreadyKnowSize() {
		return alreadyKnowSize;
	}

	public void setAlreadyKnowSize(long alreadyKnowSize) {
		this.alreadyKnowSize = alreadyKnowSize;
	}

	public long getAlmostKnowSize() {
		return almostKnowSize;
	}

	public void setAlmostKnowSize(long almostKnowSize) {
		this.almostKnowSize = almostKnowSize;
	}

	public long getKnowSize() {
		return knowSize;
	}

	public void setKnowSize(long knowSize) {
		this.knowSize = knowSize;
	}

	public long getUnclassifiedSize() {
		return unclassifiedSize;
	}

	public void setUnclassifiedSize(long unclassifiedSize) {
		this.unclassifiedSize = unclassifiedSize;
	}

	public long getTotalSize() {
		return doNotKnowSize + alreadyKnowSize + almostKnowSize + knowSize + unclassifiedSize;
	}
	
	public double getKnownPercent() {
		long knowTotal = alreadyKnowSize + almostKnowSize + knowSize;
		long total = doNotKnowSize + alreadyKnowSize + almostKnowSize + knowSize;
		if (total == 0) {
			return 0;
		}
		return (double) knowTotal / total;
	}
}
