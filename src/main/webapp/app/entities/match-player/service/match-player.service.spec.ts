import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMatchPlayer, MatchPlayer } from '../match-player.model';

import { MatchPlayerService } from './match-player.service';

describe('MatchPlayer Service', () => {
  let service: MatchPlayerService;
  let httpMock: HttpTestingController;
  let elemDefault: IMatchPlayer;
  let expectedResult: IMatchPlayer | IMatchPlayer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MatchPlayerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      shirtNumber: 0,
      isWarned: 0,
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

    it('should create a MatchPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MatchPlayer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MatchPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shirtNumber: 1,
          isWarned: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MatchPlayer', () => {
      const patchObject = Object.assign(
        {
          shirtNumber: 1,
          isWarned: 1,
        },
        new MatchPlayer()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MatchPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          shirtNumber: 1,
          isWarned: 1,
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

    it('should delete a MatchPlayer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMatchPlayerToCollectionIfMissing', () => {
      it('should add a MatchPlayer to an empty array', () => {
        const matchPlayer: IMatchPlayer = { id: 123 };
        expectedResult = service.addMatchPlayerToCollectionIfMissing([], matchPlayer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchPlayer);
      });

      it('should not add a MatchPlayer to an array that contains it', () => {
        const matchPlayer: IMatchPlayer = { id: 123 };
        const matchPlayerCollection: IMatchPlayer[] = [
          {
            ...matchPlayer,
          },
          { id: 456 },
        ];
        expectedResult = service.addMatchPlayerToCollectionIfMissing(matchPlayerCollection, matchPlayer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MatchPlayer to an array that doesn't contain it", () => {
        const matchPlayer: IMatchPlayer = { id: 123 };
        const matchPlayerCollection: IMatchPlayer[] = [{ id: 456 }];
        expectedResult = service.addMatchPlayerToCollectionIfMissing(matchPlayerCollection, matchPlayer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchPlayer);
      });

      it('should add only unique MatchPlayer to an array', () => {
        const matchPlayerArray: IMatchPlayer[] = [{ id: 123 }, { id: 456 }, { id: 2577 }];
        const matchPlayerCollection: IMatchPlayer[] = [{ id: 123 }];
        expectedResult = service.addMatchPlayerToCollectionIfMissing(matchPlayerCollection, ...matchPlayerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const matchPlayer: IMatchPlayer = { id: 123 };
        const matchPlayer2: IMatchPlayer = { id: 456 };
        expectedResult = service.addMatchPlayerToCollectionIfMissing([], matchPlayer, matchPlayer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(matchPlayer);
        expect(expectedResult).toContain(matchPlayer2);
      });

      it('should accept null and undefined values', () => {
        const matchPlayer: IMatchPlayer = { id: 123 };
        expectedResult = service.addMatchPlayerToCollectionIfMissing([], null, matchPlayer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(matchPlayer);
      });

      it('should return initial array if no MatchPlayer is added', () => {
        const matchPlayerCollection: IMatchPlayer[] = [{ id: 123 }];
        expectedResult = service.addMatchPlayerToCollectionIfMissing(matchPlayerCollection, undefined, null);
        expect(expectedResult).toEqual(matchPlayerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
