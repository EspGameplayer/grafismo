import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatchStats, MatchStats } from '../match-stats.model';

import { MatchStatsService } from './match-stats.service';

describe('MatchStats Service', () => {
  let service: MatchStatsService;
  let httpMock: HttpTestingController;
  let elemDefault: IMatchStats;
  let expectedResult: IMatchStats | IMatchStats[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatchStatsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      homePossessionTime: 0,
      awayPossessionTime: 0,
      inContestPossessionTime: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MatchStats', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MatchStats()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MatchStats', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          homePossessionTime: 1,
          awayPossessionTime: 1,
          inContestPossessionTime: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MatchStats', () => {
      const patchObject = Object.assign(
        {
          awayPossessionTime: 1,
        },
        new MatchStats()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MatchStats', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          homePossessionTime: 1,
          awayPossessionTime: 1,
          inContestPossessionTime: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MatchStats', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMatchStatsToCollectionIfMissing', () => {
      it('should add a MatchStats to an empty array', () => {
        const matchStats: IMatchStats = { id: 123 };
        expectedResult = service.addMatchStatsToCollectionIfMissing([], matchStats);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchStats);
      });

      it('should not add a MatchStats to an array that contains it', () => {
        const matchStats: IMatchStats = { id: 123 };
        const matchStatsCollection: IMatchStats[] = [
          {
            ...matchStats,
          },
          { id: 456 },
        ];
        expectedResult = service.addMatchStatsToCollectionIfMissing(matchStatsCollection, matchStats);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MatchStats to an array that doesn't contain it", () => {
        const matchStats: IMatchStats = { id: 123 };
        const matchStatsCollection: IMatchStats[] = [{ id: 456 }];
        expectedResult = service.addMatchStatsToCollectionIfMissing(matchStatsCollection, matchStats);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchStats);
      });

      it('should add only unique MatchStats to an array', () => {
        const matchStatsArray: IMatchStats[] = [{ id: 123 }, { id: 456 }, { id: 56830 }];
        const matchStatsCollection: IMatchStats[] = [{ id: 123 }];
        expectedResult = service.addMatchStatsToCollectionIfMissing(matchStatsCollection, ...matchStatsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const matchStats: IMatchStats = { id: 123 };
        const matchStats2: IMatchStats = { id: 456 };
        expectedResult = service.addMatchStatsToCollectionIfMissing([], matchStats, matchStats2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchStats);
        expect(expectedResult).toContain(matchStats2);
      });

      it('should accept null and undefined values', () => {
        const matchStats: IMatchStats = { id: 123 };
        expectedResult = service.addMatchStatsToCollectionIfMissing([], null, matchStats, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchStats);
      });

      it('should return initial array if no MatchStats is added', () => {
        const matchStatsCollection: IMatchStats[] = [{ id: 123 }];
        expectedResult = service.addMatchStatsToCollectionIfMissing(matchStatsCollection, undefined, null);
        expect(expectedResult).toEqual(matchStatsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
