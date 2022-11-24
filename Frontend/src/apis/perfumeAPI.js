const SERVER_URL = "http://j7c105.p.ssafy.io:8083";
const RECOM_URL = "http://j7c105.p.ssafy.io:8080";
const DJANGO_TEST_URL = "http://127.0.0.1:8000";

export const surveyResult = async (season, gender, longevity, accordClass) => {
  const URL = `${SERVER_URL}/survey`;

  if (longevity === "네") {
    // eslint-disable-next-line no-param-reassign
    longevity = 1;
  } else {
    // eslint-disable-next-line no-param-reassign
    longevity = 0;
  }
  console.log(season, gender, longevity, accordClass);
  const response = await fetch(URL, {
    method: "POST",
    headers: {
      "Content-type": "application/json"
    },
    body: JSON.stringify({
      season: season,
      gender: gender,
      longevity: longevity,
      accordClass: accordClass
    })
  });
  return response;
};

export const fetchMainPerfume = async () => {
  const URL = `${SERVER_URL}/main`;

  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json"
    }
  });
  return response;
};

export const fetchMainPerfumeUser = async data => {
  const URL = `${SERVER_URL}/main`;

  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });
  return response;
};

export const fetchWishListUser = async data => {
  const URL = `${SERVER_URL}/my-page/wish`;

  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });

  return response;
};

export const fetchHaveListUser = async data => {
  const URL = `${SERVER_URL}/my-page/have`;

  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });

  return response;
};

export const fetchAccordClassListUser = async data => {
  const URL = `${SERVER_URL}/my-page/analysis`;

  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });

  return response;
};

export const fetchWishToHave = async (data, idx) => {
  const URL = `${SERVER_URL}/my-page/wish/${idx}`;

  const response = await fetch(URL, {
    method: "PUT",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });

  return response;
};

export const fetchWishDelete = async (data, idx) => {
  const URL = `${SERVER_URL}/my-page/wish/delete/${idx}`;

  const response = await fetch(URL, {
    method: "PUT",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });

  return response;
};

export const fetchHaveDelete = async (data, idx) => {
  console.log(idx);
  const URL = `${SERVER_URL}/my-page/have/delete/${idx}`;
  const response = await fetch(URL, {
    method: "PUT",
    headers: {
      "Content-type": "application/json",
      Authorization: data
    }
  });
  return response;
};

export const fetchRecomSVD = async idx => {
  const URL = `${RECOM_URL}/api/recommend-svd?user_idx=${idx}`;
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-type": "application/json"
    }
  });
  return response;
};

export const fetchSearchPerfume = async ({
  gender,
  duration,
  accordClass,
  lastIdx,
  pageSize
}) => {
  const URL = `${SERVER_URL}/search`;
  console.log(gender, duration, accordClass, lastIdx, pageSize);
  const response = await fetch(URL, {
    method: "POST",
    headers: {
      "Content-type": "application/json"
    },
    body: JSON.stringify({
      gender: gender,
      duration: duration,
      accordClass: accordClass,
      lastIdx: lastIdx,
      pageSize: pageSize
    })
  });
  return response;
};

export const fetchRecommendCos = async PerfumeId => {
  const URL = `${RECOM_URL}/api/recommend-cos?perfume_idx=${PerfumeId}`;
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      // 'Access-Control-Allow-Origin':'*',
      "Content-type": "application/json"
    }
  });
  return response;
};

export const fetchRecommendNmf = async idx => {
  const URL = `${RECOM_URL}/api/recommend-nmf?user_idx=${idx}`;
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      // 'Access-Control-Allow-Origin':'*',
      "Content-type": "application/json"
    }
  });
  return response;
};

export const wishPerfume = async (data, idx) => {
  const URL = `${SERVER_URL}/detail/wish/${idx}`;
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      Authorization: data
    }
  });
  return response;
};

export const havePerfume = async (data, idx) => {
  const URL = `${SERVER_URL}/detail/have/${idx}`;
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      Authorization: data
    }
  });
  return response;
};
