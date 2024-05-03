import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styles from "./userPopUp.module.scss";
import { faUser } from "@fortawesome/free-solid-svg-icons";

const UserPopUp: React.FC<UserPopUpProps> = ({ onOff }) => {
  return (
    <section className={styles.popupMain}>
      <section className={styles.popup}>
        <FontAwesomeIcon icon={faUser} />
        <h1>정말 탈퇴하시겠습니까?</h1>
        <p>탈퇴 버튼 클릭시, 계정은 삭제되며 복구가 불가합니다.</p>
        <section className={styles.buttonSec}>
          <button className={styles.signout}>탈퇴</button>
          <button onClick={() => onOff(false)}>취소</button>
        </section>
      </section>
    </section>
  );
};

export default UserPopUp;
