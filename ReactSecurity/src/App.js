import { BrowserRouter, Route, Routes, useLocation, useNavigate } from "react-router-dom";
import IndexPage from "./pages/IndexPage/IndexPage";
import UserJoinPage from "./pages/UserJoinPage/UserJoinPage";
import UserLoginPage from "./pages/UserLoginPage/UserLoginPage";
import { instance } from "./apis/util/instance";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";
import UserProfilePage from "./pages/UserProfilePage/UserProfilePage";

// ()가 바로 붙은 애들은 바로 실행, 나머지는 정의!
// 총 실행 순서 : 그냥 출력 -> useQuery -> useEffect -> onSuccess 
function App() {

    const location = useLocation();
    const navigate = useNavigate();
    const [ authRefresh, setAuthRefresh ] = useState(true);

    // 얘는 실행인데 authRefresh가 false니까 실행 안됨
    useEffect(() => {
        if(!authRefresh) {
            setAuthRefresh(true);
        }
    }, [location.pathname]);
    
    // useQuery(["key"], 요청메소드, 객체) --> 선언만 해두면 호출, 실행 없이 최초에 한번은 무조건 실행됨 (useeffect 처럼)
    // 단 enable=false 면 안함
    // useQuery는 자동으로 서버(springboot) 에 요청보내서 정보 가져오는 용도 -> get요청에 주로 사용
    // use어쩌구.. 못알아들음 -> post, delete, '또 하난
    // 이건 실행이 아닌 정의 - usequery는 실행 그 안에 애들은 정의


    // token 을 localStorage 에서 꺼내오는 react-query
    // 로그인 이력 확인, 내 토큰이 사용가능한 토큰인지 확인용
    const accessTokenValid = useQuery(
        ["accessTokenValidQuery"],  // ["KEY", dependency] // dependency 가 변하면 springboot로 다시 요청 보낸다 
        async () => {
            setAuthRefresh(false);
            return await instance.get("/auth/access", {
                params: {
                    accessToken: localStorage.getItem("accessToken")
                }
            }); 
        }, { // springboot 에서 응답이 와야 실행
            enabled: authRefresh,
            retry: 0, // 응답이 false일 때 재시도를 안하겠다는 뜻, default = 3임
            refetchOnWindowFocusindow : false, // 다른 탭에 갔다가 지금 윈도우로 돌아왔을 때 재요청 보내지 않겠따! retry : 요청실패 시 같은 요청으로 재요청 , refetch : 완전 새로운 요청
            onError: error => { // 토큰이 유용하지 않다면 ( 로그인 안됐는데 = 토큰이 유용하지 않은데 auth 들어오려고 하면 로그인페이지로 )
                const authPaths = ["/profile"];
                for(let authPath of authPaths) {
                    if(location.pathname.startsWith(authPath)) { // authPaths 로 시작하는 경로 ( /profile 경로로 가면 )
                        //window.location.href=""; // 뒤로가기 됨, location.replace = 뒤로가기 안됨, 
                        navigate("/user/login"); // navigate 랑 window.location 차이 : navigate (상태유지 o), location.어쩌구 (상태유지x, 완전 새로운 페이지)
                        break;
                    }
                }
            }
        }
    );

    // 위에 accessTokenValidQuery 가 success 일때 이거 요청보냄
    // springboot 로 get요청 보내는 react-query
    // 사용 가능한 토큰이면 토큰ID로 user 가져오기
    const userInfo = useQuery(
        ["userInfoQuery"],
        async () => {
            return await instance.get("/user/me");
        },
        { // springboot 에서 응답이 와야 실행
            enabled: accessTokenValid.isSuccess && accessTokenValid.data?.data, 
            refetchOnWindowFocusindow :false, 
            // onSuccess: response => {
            //     console.log(response);
            // },
        }
    );

    return (
        <Routes>
            <Route path="/" element={ <IndexPage /> }/>
            <Route path="/user/join" element={ <UserJoinPage /> }/>
            <Route path="/user/login" element={ <UserLoginPage /> }/>
            <Route path="/profile" element={ <UserProfilePage /> } />

            <Route path="/admin/*" element={ <></> }/>
            <Route path="/admin/*" element={ <h1>Not Found</h1> }/>
            <Route path="*" element={ <h1>Not Found</h1> }/>
        </Routes>
    );
}

export default App;
