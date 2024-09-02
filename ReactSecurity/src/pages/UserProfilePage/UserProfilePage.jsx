import React from 'react';
/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';
import { useQueryClient } from 'react-query';

const imgBox = css`
    box-sizing: border-box;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
    width: 200px;
    height: 200px;
    box-shadow: 0px 0px 2px #00000088;
    cursor: pointer;
`;

const layout = css`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 100px auto;
    width: 1000px;
`;


function UserProfilePage(props) {
    const queryClient = useQueryClient();
    const userInfoState = queryClient.getQueryState("userInfoQuery");
    
    return (
        <div css={layout}>
            <h1>프로필</h1>
            <div css={imgBox}>
                <img src="" alt="" />
            </div> 
        </div>
    );
}

export default UserProfilePage;