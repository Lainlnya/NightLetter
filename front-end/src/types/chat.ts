export interface ChatMessageRequest {
  message: string;
}

export interface ChatMessageResponse {
  senderId: number;
  profileImageUrl: string;
  sendTime: string;
  nickname: string;
  message: string;
  sentByMe: boolean;
}
