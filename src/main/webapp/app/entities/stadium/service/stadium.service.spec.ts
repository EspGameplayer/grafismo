import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStadium, Stadium } from '../stadium.model';

import { StadiumService } from './stadium.service';

describe('Stadium Service', () => {
  let service: StadiumService;
  let httpMock: HttpTestingController;
  let elemDefault: IStadium;
  let expectedResult: IStadium | IStadium[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StadiumService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      graphicsName: 'AAAAAAA',
      location: 'AAAAAAA',
      capacity: 0,
      fieldLength: 0,
      fieldWidth: 0,
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

    it('should create a Stadium', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Stadium()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Stadium', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          graphicsName: 'BBBBBB',
          location: 'BBBBBB',
          capacity: 1,
          fieldLength: 1,
          fieldWidth: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Stadium', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          location: 'BBBBBB',
        },
        new Stadium()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Stadium', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          graphicsName: 'BBBBBB',
          location: 'BBBBBB',
          capacity: 1,
          fieldLength: 1,
          fieldWidth: 1,
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

    it('should delete a Stadium', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStadiumToCollectionIfMissing', () => {
      it('should add a Stadium to an empty array', () => {
        const stadium: IStadium = { id: 123 };
        expectedResult = service.addStadiumToCollectionIfMissing([], stadium);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stadium);
      });

      it('should not add a Stadium to an array that contains it', () => {
        const stadium: IStadium = { id: 123 };
        const stadiumCollection: IStadium[] = [
          {
            ...stadium,
          },
          { id: 456 },
        ];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, stadium);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Stadium to an array that doesn't contain it", () => {
        const stadium: IStadium = { id: 123 };
        const stadiumCollection: IStadium[] = [{ id: 456 }];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, stadium);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stadium);
      });

      it('should add only unique Stadium to an array', () => {
        const stadiumArray: IStadium[] = [{ id: 123 }, { id: 456 }, { id: 14199 }];
        const stadiumCollection: IStadium[] = [{ id: 123 }];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, ...stadiumArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stadium: IStadium = { id: 123 };
        const stadium2: IStadium = { id: 456 };
        expectedResult = service.addStadiumToCollectionIfMissing([], stadium, stadium2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stadium);
        expect(expectedResult).toContain(stadium2);
      });

      it('should accept null and undefined values', () => {
        const stadium: IStadium = { id: 123 };
        expectedResult = service.addStadiumToCollectionIfMissing([], null, stadium, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stadium);
      });

      it('should return initial array if no Stadium is added', () => {
        const stadiumCollection: IStadium[] = [{ id: 123 }];
        expectedResult = service.addStadiumToCollectionIfMissing(stadiumCollection, undefined, null);
        expect(expectedResult).toEqual(stadiumCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
