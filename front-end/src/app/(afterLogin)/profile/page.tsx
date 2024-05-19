'use client';

import Image from 'next/image';
import styles from './profile.module.scss';
import profileImage from '../../../../public/images/profile.jpg';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAngleRight, faCheck, faPencil } from '@fortawesome/free-solid-svg-icons';
import { useEffect, useState } from 'react';
import UserPopUp from '@/app/_components/user/UserPopUp';
import { deleteCookie } from '../action';
import { useRouter } from 'next/navigation';
import useStore from '@/store/date';
import getUserNickName from '@/libs/DiaryApi/getUserNickName';
import updateUserNickName from '@/libs/DiaryApi/updateUserNickName';
import Toast from '@/app/_components/post/Toast';
import { Messages } from '@/utils/msg';

const Profile: React.FC = () => {
  const [isPopupShown, setPopupShown] = useState<boolean>(false);
  const [toast, setToast] = useState<boolean>(false);
  const [error, setError] = useState<boolean>(false);
  const { username, setUserName } = useStore();
  const [canEdit, setEdit] = useState(false);
  const [nickname, setNickname] = useState('');

  const router = useRouter();

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
  };

  useEffect(() => {
    async function fetchData() {
      try {
        const { nickname } = await getUserNickName();
        setUserName(nickname);
        setNickname(nickname);
      } catch (error) {
        console.error('닉네임 불러오기 실패');
      }
    }
    fetchData();
  }, []);

  const handleEditToggle = async () => {
    if (canEdit) {
      try {
        await updateUserNickName(nickname);
        setError(false);
        setUserName(nickname);
      } catch (error) {
        setError(true);
        console.error('닉네임 업데이트 실패');
      }
      setToast(true);
    }
    setEdit(!canEdit);
  };

  return (
    <section className={styles.profile}>
      <h1>프로필</h1>
      <section>
        <Image className={styles.profileImage} src={profileImage} width={150} height={150} alt="프로필" priority />
        {canEdit ? (
          <input type="text" id="nickname" value={nickname} onChange={handleNicknameChange} />
        ) : (
          <input readOnly type="text" id="nickname" value={username ? username : '닉네임 표시 오류'} />
        )}
        <button onClick={handleEditToggle}>
          {canEdit ? <FontAwesomeIcon icon={faCheck} /> : <FontAwesomeIcon icon={faPencil} />}
        </button>
      </section>
      {toast &&
        (error ? (
          <Toast setToast={setToast} text={Messages.NICKNAME_SAVE_ERROR} />
        ) : (
          <Toast setToast={setToast} text={Messages.NICKNAME_SAVE_SUCCESS} />
        ))}
      <section className={styles.buttonSec}>
        <button
          onClick={() => {
            // 성공했는지 확인할 것
            deleteCookie();
            router.replace('/login');
          }}
        >
          로그아웃
        </button>
        <button onClick={() => setPopupShown(true)} className={styles.signout}>
          <div></div>
          <span>회원탈퇴</span>
          <FontAwesomeIcon icon={faAngleRight} />
        </button>
      </section>
      {isPopupShown && <UserPopUp onOff={setPopupShown} />}
    </section>
  );
};

export default Profile;
