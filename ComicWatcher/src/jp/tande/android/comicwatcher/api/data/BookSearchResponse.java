package jp.tande.android.comicwatcher.api.data;

import java.util.List;

import jp.tande.android.comicwatcher.db.BookInfo;

public class BookSearchResponse {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + carrier;
		result = prime * result + count;
		result = prime * result + first;
		result = prime * result + hits;
		result = prime * result + last;
		result = prime * result + page;
		result = prime * result + pageCount;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookSearchResponse other = (BookSearchResponse) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (carrier != other.carrier)
			return false;
		if (count != other.count)
			return false;
		if (first != other.first)
			return false;
		if (hits != other.hits)
			return false;
		if (last != other.last)
			return false;
		if (page != other.page)
			return false;
		if (pageCount != other.pageCount)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "BookSearchResponse [count=" + count + ", page=" + page
				+ ", first=" + first + ", last=" + last + ", hits=" + hits
				+ ", carrier=" + carrier + ", pageCount=" + pageCount
				+ ", Items=" + items + "]";
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public int getLast() {
		return last;
	}
	public void setLast(int last) {
		this.last = last;
	}
	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}
	public int getCarrier() {
		return carrier;
	}
	public void setCarrier(int carrier) {
		this.carrier = carrier;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<BookInfo> getItems() {
		return items;
	}
	public void setItems(List<BookInfo> items) {
		this.items = items;
	}
	private int count;
	private int page;
	private int first;
	private int last;
	private int hits;
	private int carrier;
	private int pageCount;
	private List<BookInfo> items;
}
