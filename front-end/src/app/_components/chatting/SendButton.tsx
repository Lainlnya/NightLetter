import { faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

type SendButtonProps = {
  newMessage: string;
  setNewMessage: (message: string) => void;
  sendMessage: () => void;
};

const SendButton: React.FC<SendButtonProps> = ({ newMessage, setNewMessage, sendMessage }) => {
  return (
    <>
      <input
        style={{ color: 'black' }}
        type="text"
        value={newMessage}
        onChange={(e) => setNewMessage(e.target.value)}
      />
      <button onClick={sendMessage}>
        <FontAwesomeIcon icon={faArrowUp} />
      </button>
    </>
  );
};

export default SendButton;
