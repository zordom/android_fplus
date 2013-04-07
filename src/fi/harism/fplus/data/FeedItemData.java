package fi.harism.fplus.data;

import java.util.Date;

public class FeedItemData {

	private String mActionsComment;
	private String mActionsLike;
	private boolean mAnimateIn = true;
	private String mCaption;
	private int mCommentsCount;
	private Date mCreatedTime;
	private String mFromId;
	private String mFromName;
	private String mIcon;
	private String mId;
	private boolean mLiked;
	private int mLikesCount;
	private String mMessage;
	private String mObjectId;
	private String mPicture;
	private String mStatusType;
	private String mStory;
	private String mType;
	private Date mUpdatedTime;

	public String getActionsComment() {
		return mActionsComment;
	}

	public String getActionsLike() {
		return mActionsLike;
	}

	public boolean getAnimateIn() {
		return mAnimateIn;
	}

	public String getCaption() {
		return mCaption;
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

	public String getIcon() {
		return mIcon;
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

	public String getStatusType() {
		return mStatusType;
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

	public void setAnimateIn(boolean animateIn) {
		mAnimateIn = animateIn;
	}

	public void setCaption(String caption) {
		mCaption = caption;
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

	public void setIcon(String icon) {
		mIcon = icon;
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

	public void setStatusType(String statusType) {
		mStatusType = statusType;
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
