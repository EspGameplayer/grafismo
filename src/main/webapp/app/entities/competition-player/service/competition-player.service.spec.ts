import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompetitionPlayer, CompetitionPlayer } from '../competition-player.model';

import { CompetitionPlayerService } from './competition-player.service';

describe('CompetitionPlayer Service', () => {
  let service: CompetitionPlayerService;
  let httpMock: HttpTestingController;
  let elemDefault: ICompetitionPlayer;
  let expectedResult: ICompetitionPlayer | ICompetitionPlayer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompetitionPlayerService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      preferredShirtNumber: 0,
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

    it('should create a CompetitionPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CompetitionPlayer()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompetitionPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          preferredShirtNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompetitionPlayer', () => {
      const patchObject = Object.assign({}, new CompetitionPlayer());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompetitionPlayer', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          preferredShirtNumber: 1,
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

    it('should delete a CompetitionPlayer', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCompetitionPlayerToCollectionIfMissing', () => {
      it('should add a CompetitionPlayer to an empty array', () => {
        const competitionPlayer: ICompetitionPlayer = { id: 123 };
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing([], competitionPlayer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competitionPlayer);
      });

      it('should not add a CompetitionPlayer to an array that contains it', () => {
        const competitionPlayer: ICompetitionPlayer = { id: 123 };
        const competitionPlayerCollection: ICompetitionPlayer[] = [
          {
            ...competitionPlayer,
          },
          { id: 456 },
        ];
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing(competitionPlayerCollection, competitionPlayer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompetitionPlayer to an array that doesn't contain it", () => {
        const competitionPlayer: ICompetitionPlayer = { id: 123 };
        const competitionPlayerCollection: ICompetitionPlayer[] = [{ id: 456 }];
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing(competitionPlayerCollection, competitionPlayer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competitionPlayer);
      });

      it('should add only unique CompetitionPlayer to an array', () => {
        const competitionPlayerArray: ICompetitionPlayer[] = [{ id: 123 }, { id: 456 }, { id: 84002 }];
        const competitionPlayerCollection: ICompetitionPlayer[] = [{ id: 123 }];
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing(competitionPlayerCollection, ...competitionPlayerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const competitionPlayer: ICompetitionPlayer = { id: 123 };
        const competitionPlayer2: ICompetitionPlayer = { id: 456 };
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing([], competitionPlayer, competitionPlayer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competitionPlayer);
        expect(expectedResult).toContain(competitionPlayer2);
      });

      it('should accept null and undefined values', () => {
        const competitionPlayer: ICompetitionPlayer = { id: 123 };
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing([], null, competitionPlayer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competitionPlayer);
      });

      it('should return initial array if no CompetitionPlayer is added', () => {
        const competitionPlayerCollection: ICompetitionPlayer[] = [{ id: 123 }];
        expectedResult = service.addCompetitionPlayerToCollectionIfMissing(competitionPlayerCollection, undefined, null);
        expect(expectedResult).toEqual(competitionPlayerCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
