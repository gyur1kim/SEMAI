import Axios from "./Axios";

export async function BoltImageListsLoader () {
  let BoltImageLists = []
  try {
    let response = await Axios.get('transition');
    BoltImageLists = response.data;
    console.log(BoltImageLists);
  }
  catch (err) {
    console.log(err)
  }
  return BoltImageLists;
}

export async function ReportListsLoader ({request}: {request: any}) {
  let ReportLists: string[] = [];
  console.log(request);
  const url = new URL(request.url);

  const search = url.search;
  console.log(`searchParams는 : ${search}`)

  try {
    let response = await Axios.get(`report/list${search}`);
    ReportLists = response.data.data;
    console.log(ReportLists);
  }
  catch (err) {
    console.log(err)
  }
  return ReportLists;
}