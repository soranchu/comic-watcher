package jp.tande.android.comicwatcher.api.data;

import java.util.ArrayList;
import java.util.List;

public class BookSeries {
	@Override
	public String toString() {
		return "BookSeries [books=" + books + ", latestVolume=" + latestVolume
				+ ", author=" + author + ", title=" + title + ", publisher="
				+ publisher + "]";
	}

	public BookSeries(BookInfo bi) {
		title = bi.getTitle();
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
		/*TODO:title if (title == null) {
			if (other.getTitle() != null)
				return false;
		} else if (!title.equals(other.getTitle()))
			return false;*/
		return true;

	}
	
	
	private List<BookInfo> books = new ArrayList<BookInfo>();
	private int latestVolume;
	
	private String author;
	private String title;
	private String publisher;
	private String booksGenreId;
	
}
