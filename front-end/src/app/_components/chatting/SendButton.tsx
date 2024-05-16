import { faArrowUp } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const SendButton = ({ newMessage, setNewMessage, sendMessage }) => {
  return (
    <>
      <input
        style={{ color: "black" }}
        type='text'
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
