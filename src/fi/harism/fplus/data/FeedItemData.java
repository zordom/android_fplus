package fi.harism.fplus.data;

import java.util.Date;

public class FeedItemData {

	private String mActionsComment;
	private String mActionsLike;
	private int mCommentsCount;
	private Date mCreatedTime;
	private String mFromId;
	private String mFromName;
	private String mId;
	private boolean mLiked;
	private int mLikesCount;
	private String mMessage;
	private String mObjectId;
	private String mPicture;
	private String mStory;
	private String mType;
	private Date mUpdatedTime;

	public String getActionsComment() {
		return mActionsComment;
	}

	public String getActionsLike() {
		return mActionsLike;
	}

	public int getCommentsCount() {
		return mCommentsCount;
	}

	public Date getCreatedTime() {
		return mCreatedTime;
	}

	public String getFromId() {
		return mFromId;
	}

	public String getFromName() {
		return mFromName;
	}

	public String getId() {
		return mId;
	}

	public boolean getLiked() {
		return mLiked;
	}

	public int getLikesCount() {
		return mLikesCount;
	}

	public String getMessage() {
		return mMessage;
	}

	public String getObjectId() {
		return mObjectId;
	}

	public String getPicture() {
		return mPicture;
	}

	public String getStory() {
		return mStory;
	}

	public String getType() {
		return mType;
	}

	public Date getUpdatedTime() {
		return mUpdatedTime;
	}

	public void setActionsComment(String actionsComment) {
		this.mActionsComment = actionsComment;
	}

	public void setActionsLike(String actionsLike) {
		this.mActionsLike = actionsLike;
	}

	public void setCommentsCount(int commentsCount) {
		this.mCommentsCount = commentsCount;
	}

	public void setCreatedTime(Date createdTime) {
		this.mCreatedTime = createdTime;
	}

	public void setFromId(String fromId) {
		this.mFromId = fromId;
	}

	public void setFromName(String fromName) {
		this.mFromName = fromName;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public void setLiked(boolean liked) {
		mLiked = liked;
	}

	public void setLikesCount(int likesCount) {
		this.mLikesCount = likesCount;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public void setObjectId(String objectId) {
		this.mObjectId = objectId;
	}

	public void setPicture(String picture) {
		this.mPicture = picture;
	}

	public void setStory(String story) {
		mStory = story;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.mUpdatedTime = updatedTime;
	}

}
