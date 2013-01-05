package jp.tande.android.comicwatcher.api.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BookSeries implements Serializable{
	private static final long serialVersionUID = 7893319474968721635L;

	@Override
	public String toString() {
		return "BookSeries [books=" + books + ", latestVolume=" + latestVolume
				+ ", author=" + author + ", title=" + title + ", publisher="
				+ publisher + "]";
	}

	public BookSeries(BookInfo bi) {
		title = bi.getBaseTitle();
		publisher = bi.getPublisherName();
		author = bi.getAuthor();
		booksGenreId = bi.getBooksGenreId();
		addBook(bi);
	}
	
	public List<BookInfo> getBooks() {
		return books;
	}
	
	public void addBook(BookInfo bi){
		books.add(bi);
		if( bi.getVolume() >= latestVolume && bi.getTitlePostFix() == null ){
			latest = bi;
			latestVolume = bi.getVolume();
		}
	}
	
	public int getLatestVolume() {
		return latestVolume;
	}
	public void setLatestVolume(int latestVolume) {
		this.latestVolume = latestVolume;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getBookGenreId() {
		return booksGenreId;
	}
	
	public boolean match(BookInfo other){
		if( other == null )return false;

		if (author == null) {
			if (other.getAuthor() != null)
				return false;
		} else if (!author.equals(other.getAuthor()))
			return false;
		/*if (booksGenreId == null) {
			if (other.getBooksGenreId() != null)
				return false;
		} else if (!booksGenreId.equals(other.getBooksGenreId()))
			return false;*/
		if (publisher == null) {
			if (other.getPublisherName() != null)
				return false;
		} else if (!publisher.equals(other.getPublisherName()))
			return false;
		/*if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;*/
		if (title == null) {
			if (other.getBaseTitle() != null)
				return false;
		} else if (!title.equals(other.getBaseTitle()))
			return false;
		return true;

	}
	
	
	private List<BookInfo> books = new ArrayList<BookInfo>();
	private BookInfo latest;
	private int latestVolume =0;
	
	private String author;
	private String title;
	private String publisher;
	private String booksGenreId;

	public BookInfo getLatest() {
		return latest;
	}

	public String getBooksGenreId() {
		return booksGenreId;
	}
	
}
